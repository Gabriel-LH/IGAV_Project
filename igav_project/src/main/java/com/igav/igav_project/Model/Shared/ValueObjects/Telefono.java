package com.igav.igav_project.Model.Shared.ValueObjects;

public record Telefono(String value) {
    public Telefono {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío.");
        }
        // Validación para celular local ej:9 dígitos empezando en 9
        if (!value.matches("^\\+?\\d{9,12}$")) {
            throw new IllegalArgumentException("El formato del teléfono no es válido.");
        }
    }
}
