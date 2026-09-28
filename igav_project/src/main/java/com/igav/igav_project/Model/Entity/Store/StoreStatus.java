package com.igav.igav_project.Model.Entity.Store;

/**
 * Estados posibles de una tienda dentro de la plataforma SaaS.
 *
 * @author IGAV Development Team
 */
public enum StoreStatus {
    /** Tienda activa con suscripción vigente */
    ACTIVA,
    /** Tienda suspendida por falta de pago o mantenimiento */
    SUSPENDIDA,
    /** Suscripción cancelada o tienda dada de baja */
    CANCELADA
}
