package com.igav.igav_project.DTO;

/**
 * DTO para la creación y registro de nuevas sedes o tiendas (RF-01).
 *
 * @author IGAV Development Team
 */
public record CreateBranchDTO(
    Long tenantId,
    String code,
    String name,
    String timezone,
    String email,
    String telefono,
    String direccion,
    String ciudad,
    boolean primary
) {}
