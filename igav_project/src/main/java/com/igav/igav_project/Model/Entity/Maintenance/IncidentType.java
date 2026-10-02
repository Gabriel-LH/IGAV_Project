package com.igav.igav_project.Model.Entity.Maintenance;

import com.fasterxml.jackson.annotation.JsonCreator;

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
    ROTURA_DANIO,
    /** Incumplimiento de la fecha fijada de devolución (penalidad por mora) */
    RETRASO_DEVOLUCION,
    /** Extravío o no devolución de la prenda por parte del cliente */
    PERDIDA_TOTAL;

    @JsonCreator
    public static IncidentType fromString(String value) {
        if (value == null) return MANCHA;
        String val = value.trim().toUpperCase().replace("Ñ", "N");
        for (IncidentType type : values()) {
            if (type.name().replace("Ñ", "N").equalsIgnoreCase(val)) {
                return type;
            }
        }
        return MANCHA;
    }
}