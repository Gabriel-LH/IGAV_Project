package com.igav.igav_project.Model.Entity.Inventory;

/**
 * Estados del ciclo de vida de una prenda en el inventario.
 * Cumple con el requerimiento RF-11 (Actualización de estado en tiempo real).
 *
 * @author IGAV Development Team
 */
public enum GarmentStatus {
    /** Prenda limpia y lista en almacén para ser alquilada o vendida */
    DISPONIBLE,
    /** Prenda reservada para un contrato de alquiler futuro */
    RESERVADO,
    /** Prenda entregada al cliente en un proceso de alquiler activo */
    ALQUILADO,
    /** Prenda en proceso de lavado y desinfección en tintorería (bloqueada 24-48h) */
    EN_TINTORERIA,
    /** Prenda en taller de costura/reparación por incidencias o manchas */
    EN_REPARACION,
    /** Prenda retirada definitivamente del inventario por desgaste o rotura */
    DADO_DE_BAJA
}
