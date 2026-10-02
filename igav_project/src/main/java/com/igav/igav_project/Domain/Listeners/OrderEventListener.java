package com.igav.igav_project.Domain.Listeners;

import com.igav.igav_project.Domain.Events.OrderCreatedEvent;
import com.igav.igav_project.Domain.Events.OrderReturnedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener desacoplado que procesa los eventos de dominio de contratos y devoluciones en segundo plano.
 */
@Component
public class OrderEventListener {

    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        System.out.println(">>> [EVENTO DE DOMINIO] Contrato Creado: " + event.codigoContrato() + " | Fecha: " + event.occurredOn());
    }

    @EventListener
    public void handleOrderReturned(OrderReturnedEvent event) {
        System.out.println(">>> [EVENTO DE DOMINIO] Devolución Procesada: " + event.codigoContrato() + " | Garantía Devuelta Neta: S/ " + event.garantiaDevueltaNeta());
    }
}
