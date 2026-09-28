package com.igav.igav_project.Model.Entity.Payment;

/**
 * Naturaleza o concepto de la transacción financiera registrada.
 * Cumple con los requerimientos RF-08 y RF-09.
 *
 * @author IGAV Development Team
 */
public enum PaymentType {
    /** Pago parcial o total del costo del servicio de alquiler o venta */
    COBRO_ORDEN,
    /** Depósito o retención inicial de garantía reembolsable */
    DEPOSITO_GARANTIA,
    /** Devolución al cliente del depósito de garantía sobrante */
    REEMBOLSO_GARANTIA,
    /** Cobro adicional por penalización de mora o restauración de prenda */
    COBRO_PENALIZACION
}
