package com.igav.igav_project.Model.Entity.Order;

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
    MIXTO
}
