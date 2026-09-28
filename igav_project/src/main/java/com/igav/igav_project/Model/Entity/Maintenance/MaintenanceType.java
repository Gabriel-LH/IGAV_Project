package com.igav.igav_project.Model.Entity.Maintenance;

/**
 * Tipo de proceso de mantenimiento aplicado a una prenda de vestuario.
 * Cumple con el requerimiento RF-05 (Bloqueo por mantenimiento y tintorería).
 *
 * @author IGAV Development Team
 */
public enum MaintenanceType {
    /** Lavado y desinfección estándar pos-alquiler (bloqueo 24-48 horas) */
    TINTORERIA_POST_ALQUILER,
    /** Reparación en taller de costura por descosido, cierre dañado o rotura */
    REPARACION_DAÑO,
    /** Mantenimiento o planchado preventivo periódico */
    MANTENIMIENTO_PREVENTIVO
}
