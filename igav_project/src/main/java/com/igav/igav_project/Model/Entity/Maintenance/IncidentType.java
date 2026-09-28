package com.igav.igav_project.Model.Entity.Maintenance;

/**
 * Clasificación de incidencias detectadas al recibir prendas devueltas de un alquiler.
 * Cumple con el requerimiento RF-13 (Cálculo automático de penales y retención de garantía).
 *
 * @author IGAV Development Team
 */
public enum IncidentType {
    /** Manchas complejas que requieren tratamiento químico en lavandería */
    MANCHA,
    /** Desgarros, cierres rotos o quemaduras que requieren costura */
    ROTURA_DAÑO,
    /** Incumplimiento de la fecha fijada de devolución (penalidad por mora) */
    RETRASO_DEVOLUCION,
    /** Extravío o no devolución de la prenda por parte del cliente */
    PERDIDA_TOTAL
}
