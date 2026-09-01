package com.igav.igav_project.Model.Shared.ValueObjects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public record DocumentoIdentidad(
    @Enumerated(EnumType.STRING)
    TipoDocumento tipo,
    String numero
) {
    public DocumentoIdentidad {
        if (tipo == null || numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("El tipo y número de documento son obligatorios.");
        }
        
        // Validacion para DNI
        if (tipo == TipoDocumento.DNI && !numero.matches("\\d{8}")) {
            throw new IllegalArgumentException("El DNI debe tener exactamente 8 dígitos numéricos.");
        }

        //Validacion para RUC
        if (tipo == TipoDocumento.RUC && !numero.matches("\\d{11}")) {
            throw new IllegalArgumentException("El DNI debe tener exactamente 11 dígitos numéricos.");
        }
    }
}