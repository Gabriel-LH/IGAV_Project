package com.igav.igav_project.Model.Entity.Order;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Tipo de operación comercial de una Orden.
 *
 * @author IGAV Development Team
 */
public enum OrderType {
    /** Contrato enfocado exclusivamente en alquiler de prendas */
    ALQUILER,
    /** Transacción de venta directa de prenda o accesorio */
    VENTA,
    /** Transacción mixta que combina alquiler y venta directa */
    MIXTO;

    @JsonCreator
    public static OrderType fromString(String value) {
        if (value == null) return ALQUILER;
        String val = value.trim().toUpperCase();
        if (val.equals("ALQUILAR") || val.equals("ALQUILER")) return ALQUILER;
        if (val.equals("VENTA") || val.equals("VENDER")) return VENTA;
        if (val.equals("MIXTO")) return MIXTO;
        return ALQUILER;
    }
}