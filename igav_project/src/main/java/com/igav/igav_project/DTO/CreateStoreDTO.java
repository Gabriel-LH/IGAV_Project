package com.igav.igav_project.DTO;

/**
 * DTO para la creación y registro de nuevas sedes o tiendas (RF-01).
 *
 * @author IGAV Development Team
 */
public record CreateStoreDTO(
    String nombreComercial,
    String razonSocial,
    String ruc,
    String email,
    String telefono,
    String direccion,
    String ciudad
) {}
