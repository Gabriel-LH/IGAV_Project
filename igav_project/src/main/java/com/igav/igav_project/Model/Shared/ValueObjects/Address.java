package com.igav.igav_project.Model.Shared.ValueObjects;

import jakarta.persistence.Embeddable;

/**
 * Value Object inmutable que representa una dirección física completa.
 *
 * @param calle Dirección específica (calle, avenida, número, manzana, lote).
 * @param distrito Distrito geográfico.
 * @param departamento Departamento / Provincia.
 * @param pais País de residencia.
 * @param codigoPostal Código postal local.
 * @param referencia Referencia de ubicación.
 * @param zipCode Código ZIP internacional.
 * @author IGAV Development Team
 */
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
    /**
     * Constructor compacto para la validación del campo mínimo obligatorio.
     *
     * @throws IllegalArgumentException si el campo calle es nulo o vacío.
     */
    public Address {
        if (calle == null || calle.isBlank()) {
            throw new IllegalArgumentException("La calle es obligatoria.");
        }
    }
}
