package com.igav.igav_project.Model.Shared.ValueObjects;

import jakarta.persistence.Embeddable;

/**
 * Value Object inmutable que representa un número telefónico.
 * Encapsula la validación de números móviles y fijos de 9 a 12 dígitos (con prefijo opcional).
 *
 * @param value Cadena numéricas que representa el teléfono.
 * @author IGAV Development Team
 */
@Embeddable
public record Telefono(String value) {

    /**
     * Constructor compacto para aplicar validaciones de formato telefónico.
     *
     * @throws IllegalArgumentException si el teléfono es nulo, está vacío o no coincide con la sintaxis esperada.
     */
    public Telefono {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El número de teléfono no puede estar vacío.");
        }
        if (!value.matches("^\\+?\\d{9,12}$")) {
            throw new IllegalArgumentException("El formato del número de teléfono no es válido.");
        }
    }
}
