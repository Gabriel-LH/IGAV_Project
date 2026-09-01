package com.igav.igav_project.Model.Shared.ValueObjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Address(
    String calle,
    String distrito,
    String departamento,
    String pais,
    String codigoPostal,
    String referencia,
    String zipCode
) {
    // Constructor compacto para validaciones de negocio
    public Address {
        if (calle == null || calle.isBlank()) {
            throw new IllegalArgumentException("La calle es obligatoria.");
        }
    }
}

