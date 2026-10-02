package com.igav.igav_project.Domain.Abstractions;

import java.time.LocalDateTime;

/**
 * Interfaz base para todos los Eventos de Dominio en la arquitectura limpia de IGAV SaaS.
 */
public interface DomainEvent {
    LocalDateTime occurredOn();
}
