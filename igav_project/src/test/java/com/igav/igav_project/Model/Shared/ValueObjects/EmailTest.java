package com.igav.igav_project.Model.Shared.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailTest {

    @Test
    void trimsAndLowercasesValidEmail() {
        Email email = new Email("  Ana.Garcia@Example.com  ");

        assertEquals("ana.garcia@example.com", email.email());
    }

    @Test
    void rejectsNullOrBlankEmail() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Email("   "));

        assertEquals("El correo electrónico no puede ser nulo o estar vacío.", exception.getMessage());
    }

    @Test
    void rejectsInvalidEmailFormat() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Email("correo-invalido"));

        assertEquals(
                "El formato del correo electrónico no es válido: correo-invalido",
                exception.getMessage());
    }
}
