package com.igav.igav_project.Model.Entity.User;

/**
 * Estados del ciclo de vida de un usuario en el sistema.
 *
 * @author IGAV Development Team
 */
public enum StatusUser {
    /** Usuario activo con acceso al sistema */
    ACTIVO,
    /** Usuario inactivo temporalmente */
    INACTIVO,
    /** Acceso suspendido por inactividad o sanción */
    SUSPENDIDO,
    /** Usuario bloqueado de manera permanente */
    BANEADO,
    /** Eliminación lógica del registro */
    ELIMINADO
}
