package com.igav.igav_project.Model.Shared.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DocumentoIdentidadTest {

    @Test
    void acceptsValidDniAndRuc() {
        assertDoesNotThrow(() -> new DocumentoIdentidad(TipoDocumento.DNI, "12345678"));
        assertDoesNotThrow(() -> new DocumentoIdentidad(TipoDocumento.RUC, "20123456789"));
    }

    @Test
    void rejectsMissingDocumentDetails() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DocumentoIdentidad(null, "12345678"));

        assertEquals("El tipo y número de documento son obligatorios.", exception.getMessage());
    }

    @Test
    void rejectsInvalidDni() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DocumentoIdentidad(TipoDocumento.DNI, "123"));

        assertEquals("El DNI debe tener exactamente 8 dígitos numéricos.", exception.getMessage());
    }

    @Test
    void rejectsInvalidRuc() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DocumentoIdentidad(TipoDocumento.RUC, "123"));

        assertEquals("El RUC debe tener exactamente 11 dígitos numéricos.", exception.getMessage());
    }
}
