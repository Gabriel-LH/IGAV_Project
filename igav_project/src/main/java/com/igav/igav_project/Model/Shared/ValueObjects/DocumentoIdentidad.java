package com.igav.igav_project.Model.Shared.ValueObjects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * Value Object inmutable que representa un documento de identidad oficial.
 * Valida formatos peruanos como DNI (8 dígitos) y RUC (11 dígitos).
 *
 * @param tipo Tipo de documento (DNI, RUC, CARNET_EXTRANJERIA, PASAPORTE).
 * @param numero Número de documento de identidad.
 * @author IGAV Development Team
 */
@Embeddable
public record DocumentoIdentidad(
    @Enumerated(EnumType.STRING)
    TipoDocumento tipo,
    String numero
) {

    /**
     * Constructor compacto para validar reglas específicas por tipo de documento.
     *
     * @throws IllegalArgumentException si los datos son nulos o no cumplen la longitud requerida.
     */
    public DocumentoIdentidad {
        if (tipo == null || numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("El tipo y número de documento son obligatorios.");
        }
        
        if (tipo == TipoDocumento.DNI && !numero.matches("\\d{8}")) {
            throw new IllegalArgumentException("El DNI debe tener exactamente 8 dígitos numéricos.");
        }

        if (tipo == TipoDocumento.RUC && !numero.matches("\\d{11}")) {
            throw new IllegalArgumentException("El RUC debe tener exactamente 11 dígitos numéricos.");
        }
    }
}