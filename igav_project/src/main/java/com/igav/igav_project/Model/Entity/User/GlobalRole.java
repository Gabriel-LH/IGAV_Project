package com.igav.igav_project.Model.Entity.User;

/**
 * Enumeración que define los roles globales y por negocio en la plataforma I.G.A.V.
 * Cumple con el requerimiento RF-02 (Gestión de roles y permisos).
 *
 * @author IGAV Development Team
 */
public enum GlobalRole {
    /** Administrador global del SaaS multitennant */
    SUPER_ADMIN,
    /** Cualquier usuario que necesita hacer un login */
    USER
}
