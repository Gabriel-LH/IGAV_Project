package com.igav.igav_project.DTO;

import com.igav.igav_project.Model.Entity.Order.OrderStatus;
import com.igav.igav_project.Model.Entity.Order.OrderType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de respuesta para representar un Contrato u Orden en la API REST.
 *
 * @author IGAV Development Team
 */
public record OrderResponseDTO(
    Long id,
    String codigoContrato,
    Long customerId,
    String clienteNombreCompleto,
    Long storeId,
    OrderType tipo,
    LocalDateTime fechaEntregaAcordada,
    LocalDateTime fechaDevolucionAcordada,
    BigDecimal subtotal,
    BigDecimal montoGarantiaTotal,
    BigDecimal descuentoGarantia,
    BigDecimal montoPenalizacion,
    BigDecimal montoTotal,
    BigDecimal garantiaDevueltaNeta,
    OrderStatus estado,
    List<OrderItemResponseDTO> items
) {
    public OrderResponseDTO(
        Long id,
        String codigoContrato,
        Long customerId,
        String clienteNombreCompleto,
        Long storeId,
        OrderType tipo,
        LocalDateTime fechaEntregaAcordada,
        LocalDateTime fechaDevolucionAcordada,
        BigDecimal subtotal,
        BigDecimal montoGarantiaTotal,
        BigDecimal descuentoGarantia,
        BigDecimal montoPenalizacion,
        BigDecimal montoTotal,
        BigDecimal garantiaDevueltaNeta,
        OrderStatus estado
    ) {
        this(id, codigoContrato, customerId, clienteNombreCompleto, storeId, tipo,
             fechaEntregaAcordada, fechaDevolucionAcordada, subtotal, montoGarantiaTotal,
             descuentoGarantia, montoPenalizacion, montoTotal, garantiaDevueltaNeta, estado, List.of());
    }
}