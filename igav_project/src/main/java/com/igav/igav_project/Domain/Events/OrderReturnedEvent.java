package com.igav.igav_project.Domain.Events;

import com.igav.igav_project.Domain.Abstractions.DomainEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Evento de Dominio emitido cuando se recepciona y procesa la devolución física de un contrato de alquiler.
 */
public record OrderReturnedEvent(
        Long orderId,
        String codigoContrato,
        BigDecimal descuentoGarantiaTotal,
        BigDecimal garantiaDevueltaNeta,
        LocalDateTime occurredOn
) implements DomainEvent {
    public OrderReturnedEvent(Long orderId, String codigoContrato, BigDecimal descuentoGarantiaTotal, BigDecimal garantiaDevueltaNeta) {
        this(orderId, codigoContrato, descuentoGarantiaTotal, garantiaDevueltaNeta, LocalDateTime.now());
    }
}
