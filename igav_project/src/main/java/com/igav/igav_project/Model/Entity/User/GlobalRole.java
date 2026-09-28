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
    /** Administrador o dueño de una tienda específica */
    ADMIN_TIENDA,
    /** Personal encargado de ventas y alquileres directos */
    VENDEDOR,
    /** Operador a cargo del inventario, lavandería y mantenimiento de prendas */
    ENCARGADO_ALMACEN_TINTORERIA
}
