package com.igav.igav_project.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para la solicitud de inicio de sesión y obtención de token JWT.
 *
 * @param username Email o nombre de usuario.
 * @param password Contraseña del usuario.
 * @author IGAV Development Team
 * @version 1.0.0
 */
@Schema(description = "Credenciales para inicio de sesión en IGAV SaaS")
public record LoginRequestDTO(
        @Schema(description = "Email o nombre de usuario del empleado/administrador", example = "admin.saas@igav.pe")
        String username,

        @Schema(description = "Contraseña de acceso", example = "admin123")
        String password
) {}
