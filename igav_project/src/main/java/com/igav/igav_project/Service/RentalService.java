package com.igav.igav_project.Service;

import com.igav.igav_project.DTO.CreateRentalRequestDTO;
import com.igav.igav_project.DTO.RentalResponseDTO;
import com.igav.igav_project.Exception.DateCollisionException;
import com.igav.igav_project.Exception.InvalidOrderStateException;
import com.igav.igav_project.Exception.ResourceNotFoundException;
import com.igav.igav_project.Model.Entity.Commerce.Guarantee.Guarantee;
import com.igav.igav_project.Model.Entity.Commerce.Guarantee.GuaranteeStatus;
import com.igav.igav_project.Model.Entity.Commerce.Operation.Operation;
import com.igav.igav_project.Model.Entity.Commerce.Operation.OperationStatus;
import com.igav.igav_project.Model.Entity.Commerce.Operation.OperationType;
import com.igav.igav_project.Model.Entity.Commerce.Rental.Rental;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalItem;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalItemStatus;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalStatus;
import com.igav.igav_project.Model.Entity.Commerce.Reservation.Reservation;
import com.igav.igav_project.Model.Entity.Commerce.Reservation.ReservationItem;
import com.igav.igav_project.Model.Entity.Commerce.Reservation.ReservationStatus;
import com.igav.igav_project.Model.Entity.Inventory.InventoryItem.InventoryCondition;
import com.igav.igav_project.Model.Entity.Inventory.InventoryItem.InventoryItem;
import com.igav.igav_project.Model.Entity.Inventory.InventoryItem.InventoryStatus;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;
import com.igav.igav_project.Model.Shared.ValueObjects.Moneda;
import com.igav.igav_project.Repository.InventoryRepository;
import com.igav.igav_project.Repository.RentalRepository;
import com.igav.igav_project.Repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class RentalService {

    private static final List<RentalStatus> BLOCKING_RENTAL_STATUSES = List.of(
            RentalStatus.ALQUILADO,
            RentalStatus.ATRASADO,
            RentalStatus.RESERVADO_FISICO);

    private final RentalRepository rentalRepository;
    private final ReservationRepository reservationRepository;
    private final InventoryRepository inventoryRepository;
    private final RentalPolicyService rentalPolicyService;
    private final RentalResponseMapper responseMapper;

    public RentalService(
            RentalRepository rentalRepository,
            ReservationRepository reservationRepository,
            InventoryRepository inventoryRepository,
            RentalPolicyService rentalPolicyService,
            RentalResponseMapper responseMapper) {
        this.rentalRepository = rentalRepository;
        this.reservationRepository = reservationRepository;
        this.inventoryRepository = inventoryRepository;
        this.rentalPolicyService = rentalPolicyService;
        this.responseMapper = responseMapper;
    }

    @Transactional
    public RentalResponseDTO createRental(CreateRentalRequestDTO request) {
        if (request == null || request.reservationId() == null) {
            throw new IllegalArgumentException("El ID de la reserva es obligatorio.");
        }
        Reservation reservation = reservationRepository.findByIdForUpdate(request.reservationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reserva no encontrada con ID: " + request.reservationId()));
        if (reservation.getStatus() != ReservationStatus.CONFIRMADO) {
            throw new InvalidOrderStateException("Solo se pueden alquilar reservas confirmadas.");
        }

        LocalDateTime reservationStart = toLocalDateTime(reservation.getStartDate());
        LocalDateTime outDate = request.fechaEntrega() != null ? request.fechaEntrega() : reservationStart;
        LocalDateTime expectedReturnDate = toLocalDateTime(reservation.getEndDate());
        if (outDate.isBefore(reservationStart) || !expectedReturnDate.isAfter(outDate)) {
            throw new IllegalArgumentException("El periodo del alquiler debe estar dentro de la reserva.");
        }

        List<ReservationItem> reservedItems = reservation.getReservationItems();
        if (reservedItems == null || reservedItems.isEmpty()) {
            throw new IllegalArgumentException("La reserva no tiene prendas para alquilar.");
        }

        Operation operation = reservation.getOperation();
        Rental rental = Rental.builder()
                .tenant(reservation.getTenant())
                .operation(operation)
                .reservation(reservation)
                .client(reservation.getClient())
                .branch(reservation.getBranch())
                .outDate(outDate)
                .expectedReturnDate(expectedReturnDate)
                .status(RentalStatus.ALQUILADO)
                .notes(request.observaciones())
                .auditMetadata(AuditMetadata.create(request.createdBy()))
                .build();

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;
        Moneda currency = null;
        for (ReservationItem reservedItem : reservedItems) {
            RentalItem rentalItem = createRentalItem(reservation, reservedItem, rental, request.createdBy(),
                    outDate, expectedReturnDate);
            rental.getRentalItems().add(rentalItem);
            subtotal = subtotal.add(rentalItem.getPriceAtMoment());
            discount = discount.add(rentalItem.getDiscountAmount());
            if (currency == null) {
                currency = reservedItem.getPriceAtMoment();
            }
        }
        if (currency == null) {
            throw new IllegalArgumentException("La reserva no tiene precios de alquiler válidos.");
        }
        rental.setSubTotal(new Moneda(subtotal, currency.tipoMoneda()));
        rental.setTotalDiscount(new Moneda(discount, currency.tipoMoneda()));

        attachGuarantees(operation, rental, request.createdBy());
        if (rentalPolicyService.getPolicy(reservation.getTenant()).requireGuarantee()
                && !rentalPolicyService.hasApplicableGuarantee(rental)) {
            throw new InvalidOrderStateException("La política del tenant requiere una garantía para iniciar el alquiler.");
        }

        if (operation != null) {
            operation.setOperationType(OperationType.ALQUILER);
            operation.setOperationStatus(OperationStatus.EN_PROGRESO);
        }
        reservation.setStatus(ReservationStatus.CONVERTIDO);
        reservationRepository.save(reservation);
        return responseMapper.toResponse(rentalRepository.save(rental));
    }

    @Transactional(readOnly = true)
    public List<RentalResponseDTO> getAllRentals() {
        return rentalRepository.findAll().stream().map(responseMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<RentalResponseDTO> getRentalsByBranch(Long branchId) {
        return rentalRepository.findByBranchId(branchId).stream().map(responseMapper::toResponse).toList();
    }

    private RentalItem createRentalItem(
            Reservation reservation,
            ReservationItem reservedItem,
            Rental rental,
            String createdBy,
            LocalDateTime outDate,
            LocalDateTime expectedReturnDate) {
        InventoryItem inventoryItem = reservedItem.getInventoryItem();
        if (!reservedItem.isSerial() || inventoryItem == null) {
            throw new IllegalArgumentException("El alquiler requiere una unidad serializada por prenda.");
        }
        InventoryItem lockedItem = inventoryRepository.findByIdForUpdate(inventoryItem.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Prenda de inventario no encontrada: " + inventoryItem.getId()));
        if (!lockedItem.isForRent()) {
            throw new IllegalArgumentException("La prenda " + lockedItem.getSerialCode()
                    + " no está habilitada para alquiler.");
        }
        if (lockedItem.getInventoryCondition() == InventoryCondition.Dañado
                || lockedItem.getInventoryCondition() == InventoryCondition.Obsoleto) {
            throw new DateCollisionException("La prenda " + lockedItem.getSerialCode()
                    + " está dañada u obsoleta y no puede alquilarse.");
        }
        if (lockedItem.getInventoryStatus() == InventoryStatus.EnLavanderia) {
            if (lockedItem.getAvailableAt() == null || lockedItem.getAvailableAt().isAfter(outDate)) {
                throw new DateCollisionException("La prenda " + lockedItem.getSerialCode()
                        + " seguirá en lavandería al iniciar el alquiler.");
            }
            lockedItem.setInventoryStatus(InventoryStatus.Disponible);
            lockedItem.setAvailableAt(null);
        } else if (lockedItem.getInventoryStatus() != InventoryStatus.Disponible
                && lockedItem.getInventoryStatus() != InventoryStatus.Reservado) {
            throw new DateCollisionException("La prenda " + lockedItem.getSerialCode()
                    + " no está disponible; estado actual: " + lockedItem.getInventoryStatus());
        }

        boolean rentalCollision = rentalRepository.existsOverlappingRental(
                lockedItem.getId(), outDate, expectedReturnDate, BLOCKING_RENTAL_STATUSES);
        boolean reservationCollision = rentalRepository.existsOverlappingConfirmedReservation(
                lockedItem.getId(), reservation.getId(), toDate(outDate), toDate(expectedReturnDate),
                ReservationStatus.CONFIRMADO);
        if (rentalCollision || reservationCollision) {
            throw new DateCollisionException("La prenda " + lockedItem.getSerialCode()
                    + " ya está reservada o alquilada durante ese periodo.");
        }

        Moneda price = reservedItem.getPriceAtMoment();
        Moneda listPrice = reservedItem.getListPrice() != null ? reservedItem.getListPrice() : price;
        Moneda lineDiscount = reservedItem.getTotalDiscount() != null
                ? reservedItem.getTotalDiscount()
                : Moneda.zero(price.tipoMoneda());
        RentalItem rentalItem = RentalItem.builder()
                .tenantId(lockedItem.getTenant().getId())
                .rental(rental)
                .operation(reservation.getOperation())
                .product(reservedItem.getProduct())
                .productVariant(reservedItem.getProductVariant())
                .inventoryItem(lockedItem)
                .priceAtMoment(price.monto())
                .quantity(BigDecimal.ONE)
                .conditionOut(lockedItem.getInventoryCondition().name())
                .isDamage(false)
                .discountAmount(lineDiscount.monto())
                .productName(reservedItem.getProduct().getNombre())
                .variantCode(reservedItem.getProductVariant().getVariantCode())
                .serialCode(lockedItem.getSerialCode())
                .isSerial(true)
                .listPrice(listPrice.monto())
                .status(RentalItemStatus.Alquilado)
                .auditMetadata(AuditMetadata.create(createdBy))
                .build();
        lockedItem.setInventoryStatus(InventoryStatus.Alquilado);
        lockedItem.setAvailableAt(null);
        inventoryRepository.save(lockedItem);
        return rentalItem;
    }

    private void attachGuarantees(Operation operation, Rental rental, String receivedBy) {
        if (operation == null) {
            return;
        }
        for (Guarantee guarantee : operation.getGuarantees()) {
            guarantee.setRental(rental);
            if (guarantee.getGuaranteeStatus() == GuaranteeStatus.Pendiente) {
                guarantee.setGuaranteeStatus(GuaranteeStatus.Custodia);
            }
            guarantee.setReceivedById(receivedBy);
            rental.getGuarantees().add(guarantee);
        }
    }

    private LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private Date toDate(LocalDateTime date) {
        return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    }
}