package com.igav.igav_project.Service;

import com.igav.igav_project.DTO.RentalReturnInspectionResponseDTO;
import com.igav.igav_project.DTO.RentalReturnItemRequestDTO;
import com.igav.igav_project.DTO.RentalReturnRequestDTO;
import com.igav.igav_project.DTO.RentalResponseDTO;
import com.igav.igav_project.Exception.InvalidOrderStateException;
import com.igav.igav_project.Exception.ResourceNotFoundException;
import com.igav.igav_project.Model.Entity.Commerce.Guarantee.GuaranteeType;
import com.igav.igav_project.Model.Entity.Commerce.Operation.OperationStatus;
import com.igav.igav_project.Model.Entity.Commerce.Rental.GuaranteeDecision;
import com.igav.igav_project.Model.Entity.Commerce.Rental.Rental;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalCharge;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalChargeStatus;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalChargeType;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalItem;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalItemStatus;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalReturnInspection;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalStatus;
import com.igav.igav_project.Model.Entity.Commerce.Rental.ReturnCondition;
import com.igav.igav_project.Model.Entity.Commerce.Rental.ReturnDisposition;
import com.igav.igav_project.Model.Entity.Inventory.InventoryItem.InventoryCondition;
import com.igav.igav_project.Model.Entity.Inventory.InventoryItem.InventoryItem;
import com.igav.igav_project.Model.Entity.Inventory.InventoryItem.InventoryStatus;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;
import com.igav.igav_project.Repository.InventoryRepository;
import com.igav.igav_project.Repository.RentalRepository;
import com.igav.igav_project.Repository.RentalReturnInspectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RentalReturnService {

    private final RentalRepository rentalRepository;
    private final InventoryRepository inventoryRepository;
    private final RentalReturnInspectionRepository inspectionRepository;
    private final RentalPolicyService rentalPolicyService;
    private final RentalResponseMapper responseMapper;

    public RentalReturnService(
            RentalRepository rentalRepository,
            InventoryRepository inventoryRepository,
            RentalReturnInspectionRepository inspectionRepository,
            RentalPolicyService rentalPolicyService,
            RentalResponseMapper responseMapper) {
        this.rentalRepository = rentalRepository;
        this.inventoryRepository = inventoryRepository;
        this.inspectionRepository = inspectionRepository;
        this.rentalPolicyService = rentalPolicyService;
        this.responseMapper = responseMapper;
    }

    @Transactional
    @SuppressWarnings("null")
    public RentalResponseDTO processReturn(Long rentalId, RentalReturnRequestDTO request) {
        if (rentalId == null) {
            throw new IllegalArgumentException("El ID del alquiler es obligatorio.");
        }
        Rental rental = rentalRepository.findByIdForUpdate(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Alquiler no encontrado con ID: " + rentalId));
        if (rental.getStatus() != RentalStatus.ALQUILADO && rental.getStatus() != RentalStatus.ATRASADO) {
            throw new InvalidOrderStateException("El alquiler ya fue devuelto o no está activo.");
        }
        if (request == null || request.inspectedBy() == null || request.inspectedBy().isBlank()) {
            throw new IllegalArgumentException("El responsable de inspección es obligatorio.");
        }
        if (request.items() == null || request.items().size() != rental.getRentalItems().size()) {
            throw new IllegalArgumentException("Debe registrarse la condición de cada ítem del alquiler.");
        }

        Map<Long, RentalReturnItemRequestDTO> requestedInspections = validateRequestedItems(rental, request.items());
        LocalDateTime returnedAt = LocalDateTime.now();
        BigDecimal guaranteeBalance = rentalPolicyService.getMonetaryGuaranteeBalance(rental);
        BigDecimal totalGuaranteeApplied = BigDecimal.ZERO;
        BigDecimal lateFee = rentalPolicyService.calculateLateFee(rental, returnedAt);
        boolean hasDamage = false;
        boolean hasLostItem = false;
        int itemCount = rental.getRentalItems().size();

        for (int index = 0; index < itemCount; index++) {
            RentalItem rentalItem = rental.getRentalItems().get(index);
            InventoryItem inventoryItem = inventoryRepository.findByIdForUpdate(rentalItem.getInventoryItem().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Prenda de inventario no encontrada: " + rentalItem.getInventoryItem().getId()));
            RentalReturnItemRequestDTO requestedInspection = requestedInspections.get(inventoryItem.getId());
            ReturnCondition condition = requestedInspection.condicionRecibida();
            BigDecimal damageCharge = requestedInspection.cargoDanio() != null
                    ? requestedInspection.cargoDanio()
                    : BigDecimal.ZERO;
            if (damageCharge.signum() < 0) {
                throw new IllegalArgumentException("El cargo por daño debe ser igual o mayor a cero.");
            }

            boolean damaged = condition == ReturnCondition.DANADA || condition == ReturnCondition.INCOMPLETA;
            boolean lost = condition == ReturnCondition.PERDIDA;
            if (!damaged && !lost && damageCharge.signum() > 0) {
                throw new IllegalArgumentException(
                        "Solo una prenda dañada, incompleta o perdida puede generar cargo por daño.");
            }
            BigDecimal assessedDamage = damaged || lost ? damageCharge : BigDecimal.ZERO;
            BigDecimal assessedLateFee = splitAmount(lateFee, index, itemCount);
            ReturnDisposition disposition = updateItemDisposition(
                    rentalItem, inventoryItem, condition, returnedAt);
            hasDamage |= damaged || lost;
            hasLostItem |= lost;

            BigDecimal itemGuaranteeApplied = BigDecimal.ZERO;
            if (assessedDamage.signum() > 0) {
                RentalChargeType type = lost ? RentalChargeType.Perdido : RentalChargeType.Daño;
                BigDecimal applied = addCharge(rental, rentalItem, type, requestedInspection.descripcion(),
                        assessedDamage, guaranteeBalance, request.inspectedBy());
                itemGuaranteeApplied = itemGuaranteeApplied.add(applied);
                guaranteeBalance = guaranteeBalance.subtract(applied);
                totalGuaranteeApplied = totalGuaranteeApplied.add(applied);
            }
            if (assessedLateFee.signum() > 0) {
                BigDecimal applied = addCharge(rental, rentalItem, RentalChargeType.TarifaRetraso,
                        "Mora calculada según la política del tenant", assessedLateFee,
                        guaranteeBalance, request.inspectedBy());
                itemGuaranteeApplied = itemGuaranteeApplied.add(applied);
                guaranteeBalance = guaranteeBalance.subtract(applied);
                totalGuaranteeApplied = totalGuaranteeApplied.add(applied);
            }

            BigDecimal totalCharge = assessedDamage.add(assessedLateFee);
            GuaranteeDecision guaranteeDecision = determineGuaranteeDecision(
                    rental, totalCharge, itemGuaranteeApplied, damaged || lost);
            RentalReturnInspection inspection = RentalReturnInspection.builder()
                    .rentalItem(rentalItem)
                    .inventoryItem(inventoryItem)
                    .receivedCondition(condition)
                    .disposition(disposition)
                    .guaranteeDecision(guaranteeDecision)
                    .damageNotes(requestedInspection.descripcion())
                    .damageCharge(assessedDamage)
                    .lateFee(assessedLateFee)
                    .guaranteeApplied(itemGuaranteeApplied)
                    .outstandingBalance(totalCharge.subtract(itemGuaranteeApplied).max(BigDecimal.ZERO))
                    .inspectedAt(returnedAt)
                    .inspectedBy(request.inspectedBy())
                    .auditMetadata(AuditMetadata.create(request.inspectedBy()))
                    .build();
            rentalItem.setReturnInspection(inspection);
            inspectionRepository.save(inspection);
            inventoryItem.setUsageCount(inventoryItem.getUsageCount() + 1);
            inventoryRepository.save(inventoryItem);
        }

        rentalPolicyService.updateGuaranteeStatuses(
                rental, totalGuaranteeApplied, hasDamage || hasLostItem, request.inspectedBy());
        rental.setActualReturnDate(returnedAt);
        rental.setStatus(hasLostItem ? RentalStatus.PERDIDO
                : hasDamage ? RentalStatus.CON_DAÑOS : RentalStatus.DEVUELTO);
        rental.setAuditMetadata(rental.getAuditMetadata().update(request.inspectedBy()));
        if (rental.getOperation() != null) {
            rental.getOperation().setOperationStatus(OperationStatus.COMPLETADO);
        }
        return responseMapper.toResponse(rentalRepository.save(rental));
    }

    @Transactional(readOnly = true)
    public List<RentalReturnInspectionResponseDTO> getReturnInspections(Long rentalId) {
        if (rentalId == null) {
            throw new IllegalArgumentException("El ID del alquiler es obligatorio.");
        }
        if (!rentalRepository.existsById(rentalId)) {
            throw new ResourceNotFoundException("Alquiler no encontrado con ID: " + rentalId);
        }
        return inspectionRepository.findByRentalId(rentalId).stream()
                .map(inspection -> new RentalReturnInspectionResponseDTO(
                        inspection.getId(),
                        inspection.getRentalItem().getId(),
                        inspection.getInventoryItem().getId(),
                        inspection.getReceivedCondition(),
                        inspection.getDisposition(),
                        inspection.getGuaranteeDecision(),
                        inspection.getDamageNotes(),
                        inspection.getDamageCharge(),
                        inspection.getLateFee(),
                        inspection.getGuaranteeApplied(),
                        inspection.getOutstandingBalance(),
                        inspection.getInspectedAt(),
                        inspection.getInspectedBy(),
                        inspection.getCompletedAt(),
                        inspection.getCompletedBy()))
                .toList();
    }

    private Map<Long, RentalReturnItemRequestDTO> validateRequestedItems(
            Rental rental, List<RentalReturnItemRequestDTO> requestedItems) {
        Map<Long, RentalReturnItemRequestDTO> requestedByInventoryId = new HashMap<>();
        for (RentalReturnItemRequestDTO item : requestedItems) {
            if (item == null || item.inventoryItemId() == null || item.condicionRecibida() == null) {
                throw new IllegalArgumentException("Cada ítem devuelto debe incluir prenda y condición recibida.");
            }
            if (requestedByInventoryId.putIfAbsent(item.inventoryItemId(), item) != null) {
                throw new IllegalArgumentException("No se puede inspeccionar más de una vez la misma prenda.");
            }
        }
        Map<Long, RentalItem> rentalItemsByInventoryId = new HashMap<>();
        for (RentalItem item : rental.getRentalItems()) {
            rentalItemsByInventoryId.put(item.getInventoryItem().getId(), item);
        }
        if (!rentalItemsByInventoryId.keySet().equals(requestedByInventoryId.keySet())) {
            throw new IllegalArgumentException("La inspección debe incluir exactamente las prendas de este alquiler.");
        }
        return requestedByInventoryId;
    }

    private ReturnDisposition updateItemDisposition(
            RentalItem rentalItem,
            InventoryItem inventoryItem,
            ReturnCondition condition,
            LocalDateTime returnedAt) {
        rentalItem.setConditionIn(condition.name());
        switch (condition) {
            case DANADA, INCOMPLETA -> {
                rentalItem.setDamage(true);
                inventoryItem.setInventoryCondition(InventoryCondition.Dañado);
                inventoryItem.setInventoryStatus(InventoryStatus.EnMantenimiento);
                rentalItem.setStatus(RentalItemStatus.EnMantenimiento);
                inventoryItem.setAvailableAt(null);
                return ReturnDisposition.MANTENIMIENTO;
            }
            case PERDIDA -> {
                rentalItem.setDamage(true);
                inventoryItem.setInventoryCondition(InventoryCondition.Dañado);
                inventoryItem.setInventoryStatus(InventoryStatus.Retirado);
                rentalItem.setStatus(RentalItemStatus.Extraviado);
                inventoryItem.setAvailableAt(null);
                return ReturnDisposition.BAJA;
            }
            case SUCIA -> {
                inventoryItem.setInventoryStatus(InventoryStatus.EnLavanderia);
                inventoryItem.setAvailableAt(returnedAt.plusHours(24));
                rentalItem.setStatus(RentalItemStatus.EnLavanderia);
                return ReturnDisposition.LAVANDERIA;
            }
            case LIMPIA -> {
                inventoryItem.setInventoryStatus(InventoryStatus.Disponible);
                inventoryItem.setAvailableAt(null);
                rentalItem.setStatus(RentalItemStatus.Devuelto);
                return null;
            }
        }
        throw new IllegalArgumentException("Condición de devolución no reconocida: " + condition);
    }

    private BigDecimal splitAmount(BigDecimal amount, int index, int count) {
        if (amount.signum() <= 0 || count == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal share = amount.divide(BigDecimal.valueOf(count), 2, RoundingMode.DOWN);
        return index == count - 1
                ? amount.subtract(share.multiply(BigDecimal.valueOf(count - 1)))
                : share;
    }

    private GuaranteeDecision determineGuaranteeDecision(
            Rental rental, BigDecimal charge, BigDecimal applied, boolean damaged) {
        if (charge.signum() == 0) {
            if (damaged && rental.getGuarantees().stream()
                    .anyMatch(guarantee -> guarantee.getGuaranteeType() != GuaranteeType.Dinero)) {
                return GuaranteeDecision.RETENIDA;
            }
            return rental.getGuarantees().isEmpty()
                    ? GuaranteeDecision.NO_APLICA
                    : GuaranteeDecision.DEVUELTA;
        }
        if (applied.compareTo(charge) >= 0) {
            return GuaranteeDecision.APLICADA;
        }
        if (applied.signum() > 0) {
            return GuaranteeDecision.APLICADA_PARCIAL;
        }
        if (damaged && rental.getGuarantees().stream()
                .anyMatch(guarantee -> guarantee.getGuaranteeType() != GuaranteeType.Dinero)) {
            return GuaranteeDecision.RETENIDA;
        }
        return rental.getGuarantees().isEmpty()
                ? GuaranteeDecision.NO_APLICA
                : GuaranteeDecision.DEVUELTA;
    }

    private BigDecimal addCharge(
            Rental rental,
            RentalItem rentalItem,
            RentalChargeType type,
            String description,
            BigDecimal amount,
            BigDecimal guaranteeAvailable,
            String updatedBy) {
        if (amount.signum() <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal covered = amount.min(guaranteeAvailable).setScale(2, RoundingMode.HALF_UP);
        BigDecimal remaining = amount.subtract(covered).max(BigDecimal.ZERO);
        RentalChargeStatus status = remaining.signum() == 0
                ? RentalChargeStatus.Cubierto
                : covered.signum() > 0 ? RentalChargeStatus.PagadoParcial : RentalChargeStatus.Pendiente;
        RentalCharge charge = RentalCharge.builder()
                .tenantId(rental.getTenant().getId())
                .rental(rental)
                .rentalItem(rentalItem)
                .operation(rental.getOperation())
                .type(type)
                .description(description)
                .amount(amount.setScale(2, RoundingMode.HALF_UP))
                .guaranteeCoveredAmount(covered)
                .remainingAmount(remaining.setScale(2, RoundingMode.HALF_UP))
                .status(status)
                .auditMetadata(AuditMetadata.create(updatedBy))
                .build();
        rentalItem.getRentalCharges().add(charge);
        return covered;
    }
}