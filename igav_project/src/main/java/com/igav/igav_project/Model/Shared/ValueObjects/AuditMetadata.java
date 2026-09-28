package com.igav.igav_project.Model.Shared.ValueObjects;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

/**
 * Value Object inmutable que representa la metainformación de auditoría para entidades JPA.
 * Almacena marcas de tiempo y responsables de creación, actualización y eliminación lógica.
 *
 * @param createdAt Marca de tiempo de la creación.
 * @param createdBy Usuario que creó la entidad.
 * @param updatedAt Marca de tiempo de la última modificación.
 * @param updatedBy Usuario que modificó la entidad.
 * @param deletedAt Marca de tiempo de la eliminación lógica.
 * @param deletedBy Usuario que realizó la eliminación lógica.
 * @author IGAV Development Team
 */
@Embeddable
public record AuditMetadata(
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime updatedAt,
    String updatedBy,
    LocalDateTime deletedAt,
    String deletedBy
) {

    /**
     * Constructor compacto de validación e inicialización por defecto.
     *
     * @throws IllegalArgumentException si el campo createdBy es nulo o está en blanco.
     */
    public AuditMetadata {
        if (createdBy == null || createdBy.isBlank()) {
            throw new IllegalArgumentException("El campo createdBy no puede ser nulo o vacío.");
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    /**
     * Método de fábrica estático para construir una instancia de auditoría inicial.
     *
     * @param createdBy Identificador o nombre del usuario creador.
     * @return Nueva instancia de {@link AuditMetadata}.
     */
    public static AuditMetadata create(String createdBy) {
        return new AuditMetadata(
            LocalDateTime.now(),
            createdBy,
            null,
            null,
            null,
            null
        );
    }

    /**
     * Retorna una nueva instancia de auditoría con la información de actualización renovada.
     *
     * @param updatedBy Identificador o nombre del usuario que realiza la modificación.
     * @return Instancia modificada de {@link AuditMetadata}.
     * @throws IllegalArgumentException si updatedBy es nulo o vacío.
     */
    public AuditMetadata update(String updatedBy) {
        if (updatedBy == null || updatedBy.isBlank()) {
            throw new IllegalArgumentException("El campo updatedBy no puede ser nulo o vacío.");
        }
        return new AuditMetadata(
            this.createdAt,
            this.createdBy,
            LocalDateTime.now(),
            updatedBy,
            this.deletedAt,
            this.deletedBy
        );
    }

    /**
     * Retorna una nueva instancia de auditoría con la información de eliminación marcada.
     *
     * @param deletedBy Identificador o nombre del usuario que efectúa el borrado.
     * @return Instancia modificada de {@link AuditMetadata}.
     * @throws IllegalArgumentException si deletedBy es nulo o vacío.
     */
    public AuditMetadata delete(String deletedBy) {
        if (deletedBy == null || deletedBy.isBlank()) {
            throw new IllegalArgumentException("El campo deletedBy no puede ser nulo o vacío.");
        }
        return new AuditMetadata(
            this.createdAt,
            this.createdBy,
            this.updatedAt,
            this.updatedBy,
            LocalDateTime.now(),
            deletedBy
        );
    }
}
