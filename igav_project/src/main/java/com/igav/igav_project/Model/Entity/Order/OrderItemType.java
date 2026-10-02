package com.igav.igav_project.Model.Entity.Order;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Distingue el modelo comercial aplicado a una prenda dentro del contrato (alquiler vs venta).
 *
 * @author IGAV Development Team
 */
public enum OrderItemType {
    ALQUILER,
    VENTA;

    @JsonCreator
    public static OrderItemType fromString(String value) {
        if (value == null) return ALQUILER;
        String val = value.trim().toUpperCase();
        if (val.equals("ALQUILAR") || val.equals("ALQUILER")) return ALQUILER;
        if (val.equals("VENTA") || val.equals("VENDER")) return VENTA;
        return ALQUILER;
    }
}