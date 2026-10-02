package com.igav.igav_project.Domain.Events;

import com.igav.igav_project.Domain.Abstractions.DomainEvent;
import java.time.LocalDateTime;

/**
 * Evento de Dominio emitido al crearse y confirmarse un nuevo Contrato de Alquiler.
 */
public record OrderCreatedEvent(
        Long orderId,
        String codigoContrato,
        Long customerId,
        Long storeId,
        LocalDateTime occurredOn
) implements DomainEvent {
    public OrderCreatedEvent(Long orderId, String codigoContrato, Long customerId, Long storeId) {
        this(orderId, codigoContrato, customerId, storeId, LocalDateTime.now());
    }
}
