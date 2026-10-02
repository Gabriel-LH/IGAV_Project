package com.igav.igav_project.Domain.Abstractions;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Componente infraestructural para la publicación de Eventos de Dominio desacoplados.
 */
@Component
public class DomainEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public DomainEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publish(DomainEvent event) {
        if (event != null) {
            eventPublisher.publishEvent(event);
        }
    }
}
