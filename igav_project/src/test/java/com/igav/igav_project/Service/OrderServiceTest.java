package com.igav.igav_project.Service;

import com.igav.igav_project.DTO.*;
import com.igav.igav_project.Exception.DateCollisionException;
import com.igav.igav_project.Model.Entity.Inventory.Category;
import com.igav.igav_project.Model.Entity.Inventory.Garment;
import com.igav.igav_project.Model.Entity.Inventory.GarmentSize;
import com.igav.igav_project.Model.Entity.Inventory.GarmentStatus;
import com.igav.igav_project.Model.Entity.Maintenance.IncidentType;
import com.igav.igav_project.Model.Entity.Maintenance.MaintenanceRecord;
import com.igav.igav_project.Model.Entity.Maintenance.ReturnIncident;
import com.igav.igav_project.Model.Entity.Order.*;
import com.igav.igav_project.Model.Entity.Store.Store;
import com.igav.igav_project.Model.Shared.ValueObjects.Address;
import com.igav.igav_project.Model.Shared.ValueObjects.DocumentoIdentidad;
import com.igav.igav_project.Model.Shared.ValueObjects.Email;
import com.igav.igav_project.Model.Shared.ValueObjects.Telefono;
import com.igav.igav_project.Model.Shared.ValueObjects.TipoDocumento;
import com.igav.igav_project.Repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para validar los flujos críticos del servicio de alquileres y devoluciones.
 * Valida detección de colisión de fechas (RF-04), bloqueo de tintorería (RF-05) y deducción de garantía (RF-08, RF-13).
 *
 * @author IGAV Development Team
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private GarmentRepository garmentRepository;
    @Mock
    private MaintenanceRecordRepository maintenanceRecordRepository;
    @Mock
    private ReturnIncidentRepository returnIncidentRepository;

    @InjectMocks
    private OrderService orderService;

    private Store store;
    private Customer customer;
    private Category category;
    private Garment garment;

    @BeforeEach
    void setUp() {
        Address address = new Address("Av. Principal 123", "Miraflores", "Lima", "Perú", "15046", "Frente al parque", "15046");
        DocumentoIdentidad ruc = new DocumentoIdentidad(TipoDocumento.RUC, "20123456789");
        Email email = new Email("contacto@tienda.com");
        Telefono telefono = new Telefono("987654321");

        store = Store.create("Tienda Gala Chic", "Gala Chic S.A.C.", ruc, email, telefono, address, "admin");

        DocumentoIdentidad dni = new DocumentoIdentidad(TipoDocumento.DNI, "76543210");
        Email emailCliente = new Email("cliente@gmail.com");
        customer = Customer.create("Juan", "Pérez", dni, emailCliente, telefono, address, store, "admin");

        category = Category.create("Ternós de Gala", "Trajes elegantes de 3 piezas", store, "admin");

        garment = Garment.create(
                "SKU-TERNO-001",
                "Ternó Ejecutivo Negro",
                "Ternó completo en lana fina",
                "Negro",
                GarmentSize.M,
                category,
                store,
                new BigDecimal("150.00"),
                new BigDecimal("500.00"),
                new BigDecimal("100.00"),
                10,
                24,
                "admin"
        );
    }

    @Test
    @DisplayName("RF-04: Debe lanzar DateCollisionException cuando la prenda ya tiene una reserva en el rango de fechas")
    void testCreateOrder_DateCollision_ThrowsException() {
        LocalDateTime entrega = LocalDateTime.now().plusDays(2);
        LocalDateTime devolucion = LocalDateTime.now().plusDays(5);

        OrderItemRequestDTO itemDto = new OrderItemRequestDTO(1L, OrderItemType.ALQUILER, new BigDecimal("150.00"), new BigDecimal("100.00"));
        CreateOrderRequestDTO request = new CreateOrderRequestDTO(1L, 1L, OrderType.ALQUILER, entrega, devolucion, "Reserva de prueba", Collections.singletonList(itemDto), "vendedor1");

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(garmentRepository.findById(1L)).thenReturn(Optional.of(garment));
        when(orderRepository.existsOverlappingReservation(any(), any(), any(), any())).thenReturn(true);

        assertThrows(DateCollisionException.class, () -> orderService.createOrder(request));
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("RF-05, RF-08, RF-13: Al procesar la devolución debe descontar de la garantía y generar el bloqueo de tintorería")
    void testProcessOrderReturn_Success_GeneratesMaintenanceBlockAndDeductsGuarantee() {
        LocalDateTime entrega = LocalDateTime.now().minusDays(3);
        LocalDateTime devolucion = LocalDateTime.now().minusDays(1);

        Order order = Order.create("ORD-12345", customer, store, OrderType.ALQUILER, entrega, devolucion, "Notas", "admin");
        OrderItem item = OrderItem.create(order, garment, OrderItemType.ALQUILER, new BigDecimal("150.00"), new BigDecimal("100.00"));
        order.addItem(item);
        order.despacharAlquiler("admin");

        IncidentRequestDTO incidencia = new IncidentRequestDTO(1L, IncidentType.MANCHA, new BigDecimal("30.00"), "Mancha de vino en la solapa");
        ReturnOrderRequestDTO request = new ReturnOrderRequestDTO(1L, BigDecimal.ZERO, Collections.singletonList(incidencia), "almacen1");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(garmentRepository.findById(1L)).thenReturn(Optional.of(garment));
        when(orderRepository.save(any())).thenReturn(order);

        OrderResponseDTO response = orderService.processOrderReturn(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("70.00"), response.garantiaDevueltaNeta());
        assertEquals(GarmentStatus.EN_TINTORERIA, garment.getEstado());
        assertEquals(1, garment.getUsosAcumulados());

        verify(maintenanceRecordRepository, times(1)).save(any(MaintenanceRecord.class));
        verify(returnIncidentRepository, times(1)).save(any(ReturnIncident.class));
    }
}
