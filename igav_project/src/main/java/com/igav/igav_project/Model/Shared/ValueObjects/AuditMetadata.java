package com.igav.igav_project.Model.Shared.ValueObjects;

import java.time.LocalDateTime;


public record AuditMetadata(
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime updatedAt,
    String updatedBy,
    LocalDateTime deletedAt,
    String deletedBy
) {

    // Constructor para la creación inicial
    public AuditMetadata {
        if (createdBy == null || createdBy.isBlank()) {
            throw new IllegalArgumentException("El campo createdBy no puede ser nulo o vacío.");
        }
        // Si es una creación nueva y no se pasaron fechas, asignamos el createdAt inicial
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    // Constructor estático de fábrica para simplificar la creación inicial
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

    // Método para  actualizar la auditoría
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

    // Método para  marcar la eliminación
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
