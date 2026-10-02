package com.igav.igav_project.DTO;

/**
 * DTO para la creación y registro de nuevo personal/usuarios de staff (RF-02).
 *
 * @author IGAV Development Team
 */
public record CreateUserDTO(
    String username,
    String nombres,
    String apellidos,
    String email,
    String rol,
    Long storeId,
    String telefono,
    String tipoDocumento,
    String numeroDocumento
) {}
