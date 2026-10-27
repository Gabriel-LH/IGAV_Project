package com.igav.igav_project.Service;

import com.igav.igav_project.DTO.CreateRentalRequestDTO;
import com.igav.igav_project.DTO.RentalReturnItemRequestDTO;
import com.igav.igav_project.DTO.RentalReturnRequestDTO;
import com.igav.igav_project.Exception.DateCollisionException;
import com.igav.igav_project.Model.Entity.Client.Client;
import com.igav.igav_project.Model.Entity.Catalog.Product.Product;
import com.igav.igav_project.Model.Entity.Catalog.Product.ProductVariant.ProductVariant;
import com.igav.igav_project.Model.Entity.Commerce.Guarantee.Guarantee;
import com.igav.igav_project.Model.Entity.Commerce.Guarantee.GuaranteeStatus;
import com.igav.igav_project.Model.Entity.Commerce.Guarantee.GuaranteeType;
import com.igav.igav_project.Model.Entity.Commerce.Operation.Operation;
import com.igav.igav_project.Model.Entity.Commerce.Operation.OperationType;
import com.igav.igav_project.Model.Entity.Commerce.Rental.Rental;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalItem;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalItemStatus;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalStatus;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalReturnInspection;
import com.igav.igav_project.Model.Entity.Commerce.Rental.ReturnCondition;
import com.igav.igav_project.Model.Entity.Commerce.Rental.ReturnDisposition;
import com.igav.igav_project.Model.Entity.Commerce.Rental.GuaranteeDecision;
import com.igav.igav_project.Model.Entity.Commerce.Reservation.Reservation;
import com.igav.igav_project.Model.Entity.Commerce.Reservation.ReservationItem;
import com.igav.igav_project.Model.Entity.Commerce.Reservation.ReservationStatus;
import com.igav.igav_project.Model.Entity.Inventory.InventoryItem.InventoryCondition;
import com.igav.igav_project.Model.Entity.Inventory.InventoryItem.InventoryItem;
import com.igav.igav_project.Model.Entity.Inventory.InventoryItem.InventoryStatus;
import com.igav.igav_project.Model.Entity.Organization.Branch.Branch;
import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;
import com.igav.igav_project.Model.Entity.Organization.Tenant.TenantPolicy.LateFeeType;
import com.igav.igav_project.Model.Entity.Organization.Tenant.TenantPolicy.RentalsPolicy;
import com.igav.igav_project.Model.Entity.Organization.Tenant.TenantPolicy.TenantPolicy;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;
import com.igav.igav_project.Model.Shared.ValueObjects.Moneda;
import com.igav.igav_project.Model.Shared.ValueObjects.NombrePersona;
import com.igav.igav_project.Model.Shared.ValueObjects.TipoMoneda;
import com.igav.igav_project.Repository.InventoryRepository;
import com.igav.igav_project.Repository.RentalRepository;
import com.igav.igav_project.Repository.ReservationRepository;
import com.igav.igav_project.Repository.RentalReturnInspectionRepository;
import com.igav.igav_project.Repository.TenantPolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private TenantPolicyRepository tenantPolicyRepository;
        @Mock
        private RentalReturnInspectionRepository returnInspectionRepository;

        private RentalService rentalService;
        private RentalReturnService rentalReturnService;

    @BeforeEach
    void setUp() {
        RentalPolicyService policyService = new RentalPolicyService(tenantPolicyRepository);
        RentalResponseMapper responseMapper = new RentalResponseMapper();
        rentalService = new RentalService(
                rentalRepository, reservationRepository, inventoryRepository, policyService, responseMapper);
        rentalReturnService = new RentalReturnService(
                rentalRepository, inventoryRepository, returnInspectionRepository, policyService, responseMapper);
    }

    @Test
        @SuppressWarnings("null")
    void createRentalRejectsAnOverlappingRental() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusDays(2);
        InventoryItem inventoryItem = org.mockito.Mockito.mock(InventoryItem.class);
        ReservationItem reservedItem = org.mockito.Mockito.mock(ReservationItem.class);
        Reservation reservation = org.mockito.Mockito.mock(Reservation.class);

        when(reservationRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(reservation));
        when(reservation.getStatus()).thenReturn(ReservationStatus.CONFIRMADO);
        when(reservation.getStartDate()).thenReturn(toDate(start));
        when(reservation.getEndDate()).thenReturn(toDate(end));
        when(reservation.getReservationItems()).thenReturn(List.of(reservedItem));
        when(reservedItem.isSerial()).thenReturn(true);
        when(reservedItem.getInventoryItem()).thenReturn(inventoryItem);
        when(inventoryRepository.findByIdForUpdate(12L)).thenReturn(Optional.of(inventoryItem));
        when(inventoryItem.getId()).thenReturn(12L);
        when(inventoryItem.isForRent()).thenReturn(true);
        when(inventoryItem.getInventoryStatus()).thenReturn(InventoryStatus.Reservado);
        when(inventoryItem.getSerialCode()).thenReturn("VEST-012");
        when(rentalRepository.existsOverlappingRental(any(), any(), any(), any())).thenReturn(true);
        when(rentalRepository.existsOverlappingConfirmedReservation(any(), any(), any(), any(), any()))
                .thenReturn(false);

        assertThrows(DateCollisionException.class,
                () -> rentalService.createRental(new CreateRentalRequestDTO(7L, start, "prueba", "tester")));
        verify(rentalRepository, never()).save(any());
    }

        @Test
        @SuppressWarnings("null")
        void createRentalConvertsReservationAndMarksInventoryAsRented() {
                LocalDateTime start = LocalDateTime.now().plusDays(1);
                LocalDateTime end = start.plusDays(2);
                Tenant tenant = org.mockito.Mockito.mock(Tenant.class);
                when(tenant.getId()).thenReturn(3L);
                Client client = org.mockito.Mockito.mock(Client.class);
                when(client.getId()).thenReturn(4L);
                when(client.getNombrePersona()).thenReturn(NombrePersona.natural("Ana", "Pérez"));
                Branch branch = org.mockito.Mockito.mock(Branch.class);
                when(branch.getId()).thenReturn(5L);
                Operation operation = org.mockito.Mockito.mock(Operation.class);
                when(operation.getOperationType()).thenReturn(OperationType.ALQUILER);
                when(operation.getGuarantees()).thenReturn(List.of());
                Product product = org.mockito.Mockito.mock(Product.class);
                when(product.getNombre()).thenReturn("Vestido de gala");
                ProductVariant variant = org.mockito.Mockito.mock(ProductVariant.class);
                when(variant.getVariantCode()).thenReturn("VEST-M");
                InventoryItem inventoryItem = InventoryItem.builder()
                                .id(12L)
                                .tenant(tenant)
                                .serialCode("VEST-012")
                                .isForRent(true)
                                .inventoryCondition(InventoryCondition.Usado)
                                .inventoryStatus(InventoryStatus.Reservado)
                                .build();
                ReservationItem reservedItem = org.mockito.Mockito.mock(ReservationItem.class);
                when(reservedItem.isSerial()).thenReturn(true);
                when(reservedItem.getInventoryItem()).thenReturn(inventoryItem);
                when(reservedItem.getPriceAtMoment())
                                .thenReturn(new Moneda(new BigDecimal("100.00"), TipoMoneda.PEN));
                when(reservedItem.getProduct()).thenReturn(product);
                when(reservedItem.getProductVariant()).thenReturn(variant);
                Reservation reservation = org.mockito.Mockito.mock(Reservation.class);
                when(reservationRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(reservation));
                when(reservation.getStatus()).thenReturn(ReservationStatus.CONFIRMADO);
                when(reservation.getStartDate()).thenReturn(toDate(start));
                when(reservation.getEndDate()).thenReturn(toDate(end));
                when(reservation.getReservationItems()).thenReturn(List.of(reservedItem));
                when(reservation.getTenant()).thenReturn(tenant);
                when(reservation.getOperation()).thenReturn(operation);
                when(reservation.getClient()).thenReturn(client);
                when(reservation.getBranch()).thenReturn(branch);
                when(inventoryRepository.findByIdForUpdate(12L)).thenReturn(Optional.of(inventoryItem));
                when(rentalRepository.existsOverlappingRental(any(), any(), any(), any())).thenReturn(false);
                when(rentalRepository.existsOverlappingConfirmedReservation(any(), any(), any(), any(), any()))
                                .thenReturn(false);
                TenantPolicy noGuaranteeRequiredPolicy = org.mockito.Mockito.mock(TenantPolicy.class);
                when(noGuaranteeRequiredPolicy.getRentalsPolicy())
                        .thenReturn(new RentalsPolicy(true, 2, LateFeeType.FIXED, 0, 3, 1, false, true, true, true));
                when(tenantPolicyRepository.findActiveByTenantId(3L)).thenReturn(Optional.of(noGuaranteeRequiredPolicy));
                when(rentalRepository.save(any())).thenAnswer(invocation -> {
                        Rental rental = invocation.getArgument(0);
                        rental.setId(9L);
                        return rental;
                });

                var response = rentalService.createRental(new CreateRentalRequestDTO(7L, start, "prueba", "tester"));

                assertEquals(RentalStatus.ALQUILADO, response.status());
                assertEquals(new BigDecimal("100.00"), response.subtotal());
                assertEquals(InventoryStatus.Alquilado, inventoryItem.getInventoryStatus());
                verify(reservationRepository).save(reservation);
                verify(inventoryRepository).save(inventoryItem);
        }

    @Test
        @SuppressWarnings("null")
    void returnAppliesCashGuaranteeAndSendsDamagedGarmentToMaintenance() {
        Tenant tenant = org.mockito.Mockito.mock(Tenant.class);
        when(tenant.getId()).thenReturn(3L);
        Client client = org.mockito.Mockito.mock(Client.class);
        when(client.getId()).thenReturn(4L);
        when(client.getNombrePersona()).thenReturn(NombrePersona.natural("Ana", "Pérez"));
        Branch branch = org.mockito.Mockito.mock(Branch.class);
        when(branch.getId()).thenReturn(5L);
        Operation operation = org.mockito.Mockito.mock(Operation.class);
        when(operation.getOperationType()).thenReturn(OperationType.ALQUILER);

        InventoryItem inventoryItem = InventoryItem.builder()
                .id(12L)
                .tenant(tenant)
                .serialCode("VEST-012")
                .inventoryCondition(InventoryCondition.Usado)
                .inventoryStatus(InventoryStatus.Alquilado)
                .usageCount(2)
                .build();
        RentalItem rentalItem = RentalItem.builder()
                .id(21L)
                .inventoryItem(inventoryItem)
                .priceAtMoment(new BigDecimal("100.00"))
                .productName("Vestido de gala")
                .serialCode("VEST-012")
                .status(RentalItemStatus.Alquilado)
                .build();
        Guarantee guarantee = Guarantee.builder()
                .id(31L)
                .guaranteeType(GuaranteeType.Dinero)
                .guaranteeStatus(GuaranteeStatus.Custodia)
                .value("50.00")
                .build();

        Rental rental = Rental.builder()
                .id(9L)
                .tenant(tenant)
                .operation(operation)
                .client(client)
                .branch(branch)
                .outDate(LocalDateTime.now().minusDays(3))
                .expectedReturnDate(LocalDateTime.now().minusHours(26))
                .status(RentalStatus.ALQUILADO)
                .subTotal(new Moneda(new BigDecimal("100.00"), TipoMoneda.PEN))
                .auditMetadata(AuditMetadata.create("tester"))
                .rentalItems(List.of(rentalItem))
                .guarantees(List.of(guarantee))
                .build();
        rentalItem.setRental(rental);

        TenantPolicy policy = org.mockito.Mockito.mock(TenantPolicy.class);
        when(policy.getRentalsPolicy()).thenReturn(
                new RentalsPolicy(true, 0, LateFeeType.FIXED, 10, 3, 1, true, true, true, true));
        when(rentalRepository.findByIdForUpdate(9L)).thenReturn(Optional.of(rental));
        when(inventoryRepository.findByIdForUpdate(12L)).thenReturn(Optional.of(inventoryItem));
        when(tenantPolicyRepository.findActiveByTenantId(3L)).thenReturn(Optional.of(policy));
        when(rentalRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = rentalReturnService.processReturn(9L, new RentalReturnRequestDTO(
                List.of(new RentalReturnItemRequestDTO(12L, ReturnCondition.DANADA,
                        new BigDecimal("80.00"), "Rotura de costura")), "tester"));

        assertEquals(RentalStatus.CON_DAÑOS, response.status());
        assertEquals(new BigDecimal("20.00"), response.mora());
        assertEquals(new BigDecimal("50.00"), response.garantiaAplicada());
        assertEquals(InventoryStatus.EnMantenimiento, inventoryItem.getInventoryStatus());
        assertEquals(InventoryCondition.Dañado, inventoryItem.getInventoryCondition());
        assertEquals(3, inventoryItem.getUsageCount());
        assertEquals(GuaranteeStatus.Retenida, guarantee.getGuaranteeStatus());
        assertEquals(RentalItemStatus.EnMantenimiento, rentalItem.getStatus());
        assertEquals(ReturnCondition.DANADA, rentalItem.getReturnInspection().getReceivedCondition());
        assertEquals(ReturnDisposition.MANTENIMIENTO, rentalItem.getReturnInspection().getDisposition());
        assertEquals(GuaranteeDecision.APLICADA_PARCIAL,
                rentalItem.getReturnInspection().getGuaranteeDecision());
        verify(returnInspectionRepository).save(any(RentalReturnInspection.class));
    }

    @Test
        @SuppressWarnings("null")
    void returnWithoutDamageSendsGarmentToLaundryForTwentyFourHours() {
        Tenant tenant = org.mockito.Mockito.mock(Tenant.class);
        when(tenant.getId()).thenReturn(3L);
        Client client = org.mockito.Mockito.mock(Client.class);
        when(client.getId()).thenReturn(4L);
        when(client.getNombrePersona()).thenReturn(NombrePersona.natural("Ana", "Pérez"));
        Branch branch = org.mockito.Mockito.mock(Branch.class);
        when(branch.getId()).thenReturn(5L);
        Operation operation = org.mockito.Mockito.mock(Operation.class);
        when(operation.getOperationType()).thenReturn(OperationType.ALQUILER);

        InventoryItem inventoryItem = InventoryItem.builder()
                .id(12L)
                .tenant(tenant)
                .serialCode("VEST-012")
                .inventoryCondition(InventoryCondition.Usado)
                .inventoryStatus(InventoryStatus.Alquilado)
                .build();
        RentalItem rentalItem = RentalItem.builder()
                .id(21L)
                .inventoryItem(inventoryItem)
                .priceAtMoment(new BigDecimal("100.00"))
                .productName("Vestido de gala")
                .serialCode("VEST-012")
                .status(RentalItemStatus.Alquilado)
                .build();
        Rental rental = Rental.builder()
                .id(9L)
                .tenant(tenant)
                .operation(operation)
                .client(client)
                .branch(branch)
                .outDate(LocalDateTime.now().minusDays(1))
                .expectedReturnDate(LocalDateTime.now().plusDays(1))
                .status(RentalStatus.ALQUILADO)
                .subTotal(new Moneda(new BigDecimal("100.00"), TipoMoneda.PEN))
                .auditMetadata(AuditMetadata.create("tester"))
                .rentalItems(List.of(rentalItem))
                .build();
        rentalItem.setRental(rental);

        when(rentalRepository.findByIdForUpdate(9L)).thenReturn(Optional.of(rental));
        when(inventoryRepository.findByIdForUpdate(12L)).thenReturn(Optional.of(inventoryItem));
        when(rentalRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = rentalReturnService.processReturn(9L, new RentalReturnRequestDTO(
                List.of(new RentalReturnItemRequestDTO(12L, ReturnCondition.SUCIA, BigDecimal.ZERO, null)),
                "tester"));

        assertEquals(RentalStatus.DEVUELTO, response.status());
        assertEquals(InventoryStatus.EnLavanderia, inventoryItem.getInventoryStatus());
        assertEquals(RentalItemStatus.EnLavanderia, rentalItem.getStatus());
        assertEquals(ReturnDisposition.LAVANDERIA, rentalItem.getReturnInspection().getDisposition());
        assertEquals(1, inventoryItem.getUsageCount());
        assertTrue(inventoryItem.getAvailableAt().isAfter(LocalDateTime.now().plusHours(23)));
        assertTrue(inventoryItem.getAvailableAt().isBefore(LocalDateTime.now().plusHours(24)));
    }

    @Test
        @SuppressWarnings("null")
    void returnWithCleanGarmentMakesInventoryAvailable() {
        Tenant tenant = org.mockito.Mockito.mock(Tenant.class);
        when(tenant.getId()).thenReturn(3L);
        Client client = org.mockito.Mockito.mock(Client.class);
        when(client.getId()).thenReturn(4L);
        when(client.getNombrePersona()).thenReturn(NombrePersona.natural("Ana", "Pérez"));
        Branch branch = org.mockito.Mockito.mock(Branch.class);
        when(branch.getId()).thenReturn(5L);
        Operation operation = org.mockito.Mockito.mock(Operation.class);
        when(operation.getOperationType()).thenReturn(OperationType.ALQUILER);

        InventoryItem inventoryItem = InventoryItem.builder()
                .id(12L)
                .tenant(tenant)
                .serialCode("VEST-012")
                .inventoryCondition(InventoryCondition.Usado)
                .inventoryStatus(InventoryStatus.Alquilado)
                .build();
        RentalItem rentalItem = RentalItem.builder()
                .id(21L)
                .inventoryItem(inventoryItem)
                .priceAtMoment(new BigDecimal("100.00"))
                .productName("Vestido de gala")
                .serialCode("VEST-012")
                .status(RentalItemStatus.Alquilado)
                .build();
        Rental rental = Rental.builder()
                .id(9L)
                .tenant(tenant)
                .operation(operation)
                .client(client)
                .branch(branch)
                .outDate(LocalDateTime.now().minusDays(1))
                .expectedReturnDate(LocalDateTime.now().plusDays(1))
                .status(RentalStatus.ALQUILADO)
                .subTotal(new Moneda(new BigDecimal("100.00"), TipoMoneda.PEN))
                .auditMetadata(AuditMetadata.create("tester"))
                .rentalItems(List.of(rentalItem))
                .build();
        rentalItem.setRental(rental);

        when(rentalRepository.findByIdForUpdate(9L)).thenReturn(Optional.of(rental));
        when(inventoryRepository.findByIdForUpdate(12L)).thenReturn(Optional.of(inventoryItem));
        when(rentalRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = rentalReturnService.processReturn(9L, new RentalReturnRequestDTO(
                List.of(new RentalReturnItemRequestDTO(12L, ReturnCondition.LIMPIA, BigDecimal.ZERO, null)),
                "tester"));

        assertEquals(RentalStatus.DEVUELTO, response.status());
        assertEquals(InventoryStatus.Disponible, inventoryItem.getInventoryStatus());
        assertEquals(RentalItemStatus.Devuelto, rentalItem.getStatus());
        assertEquals(null, rentalItem.getReturnInspection().getDisposition());
    }

    @Test
    void returnRequiresAnInspectionForEveryRentalItem() {
        RentalItem rentalItem = org.mockito.Mockito.mock(RentalItem.class);
        Rental rental = org.mockito.Mockito.mock(Rental.class);
        when(rental.getStatus()).thenReturn(RentalStatus.ALQUILADO);
        when(rental.getRentalItems()).thenReturn(List.of(rentalItem));
        when(rentalRepository.findByIdForUpdate(9L)).thenReturn(Optional.of(rental));

        assertThrows(IllegalArgumentException.class,
                () -> rentalReturnService.processReturn(9L, new RentalReturnRequestDTO(List.of(), "tester")));
        verify(inventoryRepository, never()).findByIdForUpdate(any());
    }

    private Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}