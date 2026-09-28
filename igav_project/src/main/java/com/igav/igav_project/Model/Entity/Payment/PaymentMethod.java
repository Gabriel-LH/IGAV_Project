package com.igav.igav_project.Model.Entity.Payment;

/**
 * Métodos de pago aceptados por la plataforma I.G.A.V.
 * Cumple con el requerimiento RF-09 (Registro de pagos por múltiples medios).
 *
 * @author IGAV Development Team
 */
public enum PaymentMethod {
    /** Pago en efectivo física en caja de tienda */
    EFECTIVO,
    /** Transferencia bancaria o CCI */
    TRANSFERENCIA,
    /** Tarjeta de Crédito o Débito vía POS o Pasarela */
    TARJETA_CREDITO_DEBITO,
    /** Billetera digital local (Yape, Plin, Tunki, etc.) */
    BILLETERA_DIGITAL_YAPE_PLIN
}
