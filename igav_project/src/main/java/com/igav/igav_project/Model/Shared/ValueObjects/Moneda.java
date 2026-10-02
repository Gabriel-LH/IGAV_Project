package com.igav.igav_project.Model.Shared.ValueObjects;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable 
public record Moneda(BigDecimal monto, TipoMoneda tipoMoneda) {

    // Constructor compacto de Java para validaciones
    public Moneda {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto de la moneda no puede ser negativo o nulo.");
        }
        Objects.requireNonNull(tipoMoneda, "El tipo de moneda es obligatorio.");
    }

    public static Moneda zero(TipoMoneda tipoMoneda) {
        return new Moneda(BigDecimal.ZERO, tipoMoneda);
    }

    public boolean isZero() {
        return monto.compareTo(BigDecimal.ZERO) == 0;
    }

    // En Java usamos métodos descriptivos en lugar de sobrecarga de operadores (+)
    public Moneda plus(Moneda other) {
        if (!this.tipoMoneda.equals(other.tipoMoneda())) {
            throw new IllegalStateException("No se pueden sumar monedas de diferentes tipos.");
        }
        return new Moneda(this.monto.add(other.monto()), this.tipoMoneda);
    }

    // Reemplazo para el operador (-)
    public Moneda minus(Moneda other) {
        if (!this.tipoMoneda.equals(other.tipoMoneda())) {
            throw new IllegalStateException("No se pueden restar monedas de diferentes tipos.");
        }
        BigDecimal nuevoMonto = this.monto.subtract(other.monto());
        if (nuevoMonto.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("El resultado de la operación no puede ser negativo.");
        }
        return new Moneda(nuevoMonto, this.tipoMoneda);
    }

    // Como Java no soporta conversiones implícitas, usamos un método estático alternativo
    public static Moneda from(int v, TipoMoneda tipoMoneda) {
        return new Moneda(BigDecimal.valueOf(v), tipoMoneda);
    }
}