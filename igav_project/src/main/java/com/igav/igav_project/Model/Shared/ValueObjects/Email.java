package com.igav.igav_project.Model.Shared.ValueObjects;

import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;

/**
 * Value Object inmutable que representa una dirección de correo electrónico válida.
 * Encapsula las reglas de validación de sintaxis y la normalización a minúsculas.
 * 
 * @param email Cadena de texto con la dirección de correo electrónico.
 * @author IGAV Development Team
 */
@Embeddable
public record Email(String email) {

    /**
     * Patrón de expresión regular estándar para validar sintaxis de direcciones email.
     */
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    /**
     * Constructor compacto de validación y normalización.
     *
     * @throws IllegalArgumentException si el email es nulo, está vacío o no cumple con el formato válido.
     */
    public Email {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El correo electrónico no puede ser nulo o estar vacío.");
        }
        
        email = email.trim().toLowerCase();

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("El formato del correo electrónico no es válido: " + email);
        }
    }
}
