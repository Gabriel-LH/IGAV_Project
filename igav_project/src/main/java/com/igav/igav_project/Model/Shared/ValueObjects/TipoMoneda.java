package com.igav.igav_project.Model.Shared.ValueObjects;


import java.util.List;

import jakarta.persistence.Embeddable;

@Embeddable 
public record TipoMoneda(String codigo) {

    public static final TipoMoneda NONE = new TipoMoneda("");
    public static final TipoMoneda PEN = new TipoMoneda("PEN");
    public static final TipoMoneda USD = new TipoMoneda("USD");
    public static final TipoMoneda EUR = new TipoMoneda("EUR");

    // Colección de referencia inmutable equivalente a IReadOnlyCollection
    public static final List<TipoMoneda> ALL = List.of(PEN, USD, EUR);

    // Método de búsqueda equivalente a FirstOrDefault
    public static TipoMoneda fromCodigo(String codigo) {
        return ALL.stream()
                .filter(x -> x.codigo().equalsIgnoreCase(codigo))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("El código de moneda no es válido: " + codigo));
    }
}