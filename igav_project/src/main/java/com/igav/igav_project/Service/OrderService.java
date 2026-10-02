package com.igav.igav_project.Service;

import com.igav.igav_project.DTO.*;
import com.igav.igav_project.Exception.DateCollisionException;
import com.igav.igav_project.Exception.InvalidOrderStateException;
import com.igav.igav_project.Exception.ResourceNotFoundException;
import com.igav.igav_project.Model.Entity.Inventory.Garment;
import com.igav.igav_project.Model.Entity.Inventory.GarmentStatus;
import com.igav.igav_project.Model.Entity.Maintenance.MaintenanceRecord;
import com.igav.igav_project.Model.Entity.Maintenance.MaintenanceType;
import com.igav.igav_project.Model.Entity.Maintenance.ReturnIncident;
import com.igav.igav_project.Model.Entity.Order.*;
import com.igav.igav_project.Model.Entity.Store.Store;
import com.igav.igav_project.Repository.*;
import com.igav.igav_project.Domain.Abstractions.DomainEventPublisher;
import com.igav.igav_project.Domain.Events.OrderCreatedEvent;
import com.igav.igav_project.Domain.Events.OrderReturnedEvent;
import com.igav.igav_project.Model.Entity.Inventory.GarmentStatusHistory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Servicio de negocio principal para la gestión de Contratos de Alquiler,
 * Ventas y Liquidación de Garantías.
 * Cumple rigurosamente con los requerimientos RF-04, RF-05, RF-06, RF-08,
 * RF-11, RF-12 y RF-13.
 *
 * @author IGAV Development Team
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;
    private final GarmentRepository garmentRepository;
    private final MaintenanceRecordRepository maintenanceRecordRepository;
    private final ReturnIncidentRepository returnIncidentRepository;
    private final GarmentStatusHistoryRepository garmentStatusHistoryRepository;
    private final DomainEventPublisher eventPublisher;

    public OrderService(
            OrderRepository orderRepository,
            StoreRepository storeRepository,
            CustomerRepository customerRepository,
            GarmentRepository garmentRepository,
            MaintenanceRecordRepository maintenanceRecordRepository,
            ReturnIncidentRepository returnIncidentRepository,
            GarmentStatusHistoryRepository garmentStatusHistoryRepository,
            DomainEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.storeRepository = storeRepository;
        this.customerRepository = customerRepository;
        this.garmentRepository = garmentRepository;
        this.maintenanceRecordRepository = maintenanceRecordRepository;
        this.returnIncidentRepository = returnIncidentRepository;
        this.garmentStatusHistoryRepository = garmentStatusHistoryRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Registra un nuevo contrato de alquiler u orden de venta con validación de
     * colisión de fechas (RF-04, RF-06, RF-08).
     *
     * @param request Datos de la orden a crear.
     * @return DTO de respuesta con los totales calculados.
     * @throws ResourceNotFoundException si la tienda, cliente o alguna prenda no
     *                                   existen.
     * @throws DateCollisionException    si se detecta solapamiento de fechas en
     *                                   alquiler (RF-04).
     */
    @Transactional
    public OrderResponseDTO createOrder(CreateOrderRequestDTO request) {
        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new ResourceNotFoundException("Tienda no encontrada con ID: " + request.storeId()));

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Cliente no encontrado con ID: " + request.customerId()));

        String codigoContrato = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Order order = Order.create(
                codigoContrato,
                customer,
                store,
                request.tipo(),
                request.fechaEntregaAcordada(),
                request.fechaDevolucionAcordada(),
                request.observaciones(),
                request.createdBy());

        List<OrderStatus> estadosIgnorados = Arrays.asList(OrderStatus.CANCELADA, OrderStatus.BORRADOR);

        for (OrderItemRequestDTO itemDto : request.items()) {
            Garment garment = garmentRepository.findById(itemDto.garmentId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Prenda no encontrada con ID: " + itemDto.garmentId()));

            // Validación de colisión de fechas para ítems de alquiler (RF-04)
            if (itemDto.tipoItem() == OrderItemType.ALQUILER && request.fechaDevolucionAcordada() != null) {
                boolean hasCollision = orderRepository.existsOverlappingReservation(
                        garment.getId(),
                        request.fechaEntregaAcordada(),
                        request.fechaDevolucionAcordada(),
                        estadosIgnorados);

                if (hasCollision) {
                    throw new DateCollisionException(
                            "La prenda '" + garment.getNombre() + "' (SKU: " + garment.getCodigoUnico()
                                    + ") ya cuenta con una reserva confirmada en las fechas seleccionadas.");
                }

                boolean hasMaintenance = maintenanceRecordRepository.existsActiveMaintenanceInDateRange(
                        garment.getId(),
                        request.fechaEntregaAcordada(),
                        request.fechaDevolucionAcordada());

                if (hasMaintenance) {
                    throw new DateCollisionException("La prenda '" + garment.getNombre()
                            + "' se encuentra bloqueada por ciclo de tintorería/mantenimiento en el periodo solicitado.");
                }
            }

            BigDecimal precio = itemDto.precioAplicado() != null ? itemDto.precioAplicado()
                    : garment.getPrecioAlquiler();
            BigDecimal garantia = itemDto.garantiaAplicada() != null ? itemDto.garantiaAplicada()
                    : garment.getDepositoGarantia();

            OrderItem item = OrderItem.create(order, garment, itemDto.tipoItem(), precio, garantia);
            order.addItem(item);
        }

        order.confirmarOrden(request.createdBy());
        Order savedOrder = orderRepository.save(order);
        eventPublisher.publish(new OrderCreatedEvent(savedOrder.getId(), savedOrder.getCodigoContrato(),
                customer.getId(), store.getId()));

        return mapToResponseDTO(savedOrder);
    }

    /**
     * Procesa la devolución física de un contrato de alquiler:
     * 1. Registra incidencias y deduce montos de garantía (RF-08, RF-13).
     * 2. Incrementa contador de usos/lavados (RF-12).
     * 3. Genera automáticamente un bloqueo de tintorería de 24 a 48 horas (RF-05).
     * 4. Actualiza estado de prendas a EN_TINTORERIA (RF-11).
     *
     * @param request Datos de devolución e incidencias.
     * @return DTO de la orden actualizada.
     * @throws InvalidOrderStateException si la orden no está en un estado válido
     *                                    para devolución.
     */
    @Transactional
    public OrderResponseDTO processOrderReturn(ReturnOrderRequestDTO request) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Contrato u orden no encontrada con ID: " + request.orderId()));

        if (order.getEstado() != OrderStatus.EN_ALQUILER && order.getEstado() != OrderStatus.CONFIRMADA) {
            throw new InvalidOrderStateException(
                    "La orden debe estar en estado EN_ALQUILER o CONFIRMADA para procesar la devolución.");
        }

        LocalDateTime fechaDevolucionReal = LocalDateTime.now();
        BigDecimal totalDescuentoGarantia = BigDecimal.ZERO;

        // Procesamiento de incidencias (manchas, roturas, retrasos, RF-13)
        if (request.incidencias() != null && !request.incidencias().isEmpty()) {
            for (IncidentRequestDTO incDto : request.incidencias()) {
                Garment garment = garmentRepository.findById(incDto.garmentId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Prenda no encontrada con ID: " + incDto.garmentId()));

                ReturnIncident incident = ReturnIncident.create(
                        order,
                        garment,
                        incDto.tipoIncidencia(),
                        incDto.montoDescuentoGarantia(),
                        incDto.descripcion(),
                        request.updatedBy());
                returnIncidentRepository.save(incident);
                totalDescuentoGarantia = totalDescuentoGarantia.add(incDto.montoDescuentoGarantia());
            }
        }

        BigDecimal montoPenalizacion = request.montoPenalizacionMora() != null ? request.montoPenalizacionMora()
                : BigDecimal.ZERO;
        order.registrarDevolucion(fechaDevolucionReal, totalDescuentoGarantia, montoPenalizacion, request.updatedBy());

        // Actualización de prendas e inicio automático del bloqueo de tintorería
        // (RF-05, RF-11, RF-12)
        for (OrderItem item : order.getItems()) {
            if (item.getTipoItem() == OrderItemType.ALQUILER) {
                Garment garment = item.getGarment();

                // Incremento de trazabilidad de uso (RF-12)
                garment.registrarUso(request.updatedBy());

                // Cálculo del periodo de bloqueo en horas (RF-05)
                int horasBloqueo = garment.getHorasTintoreriaBloqueo() > 0 ? garment.getHorasTintoreriaBloqueo() : 24;
                LocalDateTime fechaFinTintoreria = fechaDevolucionReal.plusHours(horasBloqueo);

                // Generar registro de mantenimiento/tintorería automático (RF-05)
                MaintenanceRecord record = MaintenanceRecord.create(
                        garment,
                        order,
                        MaintenanceType.TINTORERIA_POST_ALQUILER,
                        fechaFinTintoreria,
                        BigDecimal.ZERO,
                        "Bloqueo automático pos-alquiler (Tintorería / Lavandería)",
                        "Personal de Almacén",
                        request.updatedBy());
                maintenanceRecordRepository.save(record);

                // Trazabilidad de historia de estado (Patrón Extraído de C#)
                GarmentStatusHistory history = GarmentStatusHistory.create(
                        garment,
                        GarmentStatus.ALQUILADO,
                        GarmentStatus.EN_TINTORERIA,
                        "Devolución de contrato " + order.getCodigoContrato() + " - Bloqueo de lavandería preventivo",
                        request.updatedBy());
                garmentStatusHistoryRepository.save(history);

                // Cambio de estado a EN_TINTORERIA (RF-11)
                garment.enviarATintoreria(request.updatedBy());
                garmentRepository.save(garment);
            }
        }

        Order updatedOrder = orderRepository.save(order);
        BigDecimal garantiaDevuelta = mapToResponseDTO(updatedOrder).garantiaDevueltaNeta();
        eventPublisher.publish(new OrderReturnedEvent(updatedOrder.getId(), updatedOrder.getCodigoContrato(),
                totalDescuentoGarantia, garantiaDevuelta));
        return mapToResponseDTO(updatedOrder);
    }

    /**
     * Obtiene el listado completo de órdenes y contratos registrados.
     */
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    /**
     * Obtiene las órdenes pertenecientes a una tienda específica.
     */
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByStore(Long storeId) {
        return orderRepository.findByStoreId(storeId).stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    /**
     * Mapea una entidad {@link Order} a su DTO de respuesta calculando la garantía
     * devuelta neta.
     */
    private OrderResponseDTO mapToResponseDTO(Order order) {
        BigDecimal garantiaDevueltaNeta = order.getMontoGarantiaTotal()
                .subtract(order.getDescuentoGarantia())
                .subtract(order.getMontoPenalizacion());

        if (garantiaDevueltaNeta.compareTo(BigDecimal.ZERO) < 0) {
            garantiaDevueltaNeta = BigDecimal.ZERO;
        }

        List<OrderItemResponseDTO> itemDTOs = order.getItems() != null ? order.getItems().stream()
                .map(item -> new OrderItemResponseDTO(
                        item.getId(),
                        item.getGarment() != null ? item.getGarment().getId() : null,
                        item.getGarment() != null ? item.getGarment().getNombre() : "Prenda",
                        item.getGarment() != null ? item.getGarment().getCodigoUnico() : "SKU",
                        item.getTipoItem(),
                        item.getPrecioAplicado(),
                        item.getGarantiaAplicada()
                ))
                .toList() : List.of();

        return new OrderResponseDTO(
                order.getId(),
                order.getCodigoContrato(),
                order.getCustomer().getId(),
                order.getCustomer().getNombres() + " " + order.getCustomer().getApellidos(),
                order.getStore().getId(),
                order.getTipo(),
                order.getFechaEntregaAcordada(),
                order.getFechaDevolucionAcordada(),
                order.getSubtotal(),
                order.getMontoGarantiaTotal(),
                order.getDescuentoGarantia(),
                order.getMontoPenalizacion(),
                order.getMontoTotal(),
                garantiaDevueltaNeta,
                order.getEstado(),
                itemDTOs);
    }
}
