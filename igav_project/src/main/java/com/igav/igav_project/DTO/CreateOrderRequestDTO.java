package com.igav.igav_project.DTO;

import com.igav.igav_project.Model.Entity.Order.OrderType;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para la creación de solicitudes de contratos de alquiler u órdenes de venta (RF-04, RF-06).
 *
 * @author IGAV Development Team
 */
public record CreateOrderRequestDTO(
    Long storeId,
    Long customerId,
    OrderType tipo,
    LocalDateTime fechaEntregaAcordada,
    LocalDateTime fechaDevolucionAcordada,
    String observaciones,
    List<OrderItemRequestDTO> items,
    String createdBy
) {}
