package com.igav.igav_project.Model.Entity.Order;

/**
 * Estados del ciclo de vida de un contrato u orden de alquiler/venta.
 * Cumple con los requerimientos RF-04, RF-06 y RF-11.
 *
 * @author IGAV Development Team
 */
public enum OrderStatus {
    /** Borrador preliminar de la orden antes de ser confirmada */
    BORRADOR,
    /** Orden reservada y confirmada con fecha futura de entrega */
    CONFIRMADA,
    /** Prendas entregadas al cliente; contrato en periodo activo de alquiler */
    EN_ALQUILER,
    /** Prendas devueltas por el cliente; pendientes de revisión e inspección en lavandería */
    DEVUELTO_PENDIENTE_TINTORERIA,
    /** Orden cerrada exitosamente tras liquidación de garantía y pagos */
    COMPLETADA,
    /** Orden cancelada por el cliente o la tienda */
    CANCELADA
}
