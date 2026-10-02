package com.igav.igav_project.Model.Entity.Order;

import com.fasterxml.jackson.annotation.JsonCreator;

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
    CANCELADA;

    @JsonCreator
    public static OrderStatus fromString(String value) {
        if (value == null) return CONFIRMADA;
        String val = value.trim().toUpperCase();
        if (val.equals("EN_ALQUILAR") || val.equals("EN_ALQUILER")) return EN_ALQUILER;
        for (OrderStatus s : values()) {
            if (s.name().equalsIgnoreCase(val)) return s;
        }
        return CONFIRMADA;
    }
}