package com.igav.igav_project.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO que encapsula la respuesta de autenticación exitosa con el token JWT y datos de sesión.
 *
 * @param token Token JWT firmado para incluir en cabecera Authorization: Bearer.
 * @param tokenType Tipo de token (siempre "Bearer").
 * @param email Correo electrónico del usuario autenticado.
 * @param nombreCompleto Nombre completo del usuario.
 * @param rol Rol global o de sede asignado.
 * @param storeId Identificador de la tienda asociada (o null para Super Admins).
 * @param storeNombre Nombre descriptivo de la tienda asignada.
 * @param expiresInMs Tiempo de validez del token en milisegundos.
 * @author IGAV Development Team
 * @version 1.0.0
 */
@Schema(description = "Respuesta de autenticación con token JWT y perfil de usuario")
public record AuthResponseDTO(
        @Schema(description = "Token JWT generado", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token,

        @Schema(description = "Tipo de autorización", example = "Bearer")
        String tokenType,

        @Schema(description = "Email del usuario", example = "admin.saas@igav.pe")
        String email,

        @Schema(description = "Nombre completo del usuario", example = "Admin SaaS Root")
        String nombreCompleto,

        @Schema(description = "Rol del usuario en el sistema", example = "SUPER_ADMIN")
        String rol,

        @Schema(description = "ID de la tienda vinculada", example = "1")
        Long storeId,

        @Schema(description = "Nombre de la tienda vinculada", example = "Sede Central - San Isidro")
        String storeNombre,

        @Schema(description = "Tiempo de expiración en milisegundos (24 horas)", example = "86400000")
        long expiresInMs
) {}
