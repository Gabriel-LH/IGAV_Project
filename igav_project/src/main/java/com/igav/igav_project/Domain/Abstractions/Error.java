package com.igav.igav_project.Domain.Abstractions;

/**
 * Record que representa un Error de Dominio con Código y Descripción explícitos.
 */
public record Error(String code, String description) {
    public static final Error NONE = new Error("", "");
    public static final Error NULL_VALUE = new Error("Error.NullValue", "El valor proporcionado no puede ser nulo.");
}
