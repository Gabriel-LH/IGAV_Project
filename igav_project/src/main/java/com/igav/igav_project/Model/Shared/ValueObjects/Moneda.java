package com.igav.igav_project.Model.Shared.ValueObjects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import jakarta.persistence.Embeddable;

@Embeddable 
public record Moneda(BigDecimal monto, TipoMoneda tipoMoneda) {

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

    public Moneda plus(Moneda other) {
        if (!this.tipoMoneda.equals(other.tipoMoneda())) {
            throw new IllegalStateException("No se pueden sumar monedas de diferentes tipos.");
        }
        return new Moneda(this.monto.add(other.monto()), this.tipoMoneda);
    }

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

    public static Moneda from(int v, TipoMoneda tipoMoneda) {
        return new Moneda(BigDecimal.valueOf(v), tipoMoneda);
    }

    // --- MÉTODOS DE OPERACIÓN AÑADIDOS ---

    // Multiplica el monto por una cantidad entera
    public Moneda multiply(int factor) {
        return new Moneda(this.monto.multiply(BigDecimal.valueOf(factor)), this.tipoMoneda);
    }

    // Dividir el monto entre una cantidad entera con escala y redondeo especificados
    public Moneda divide(int divisor, int scale, RoundingMode roundingMode) {
        if (divisor <= 0) {
            throw new IllegalArgumentException("El divisor debe ser mayor a cero.");
        }
        BigDecimal nuevoMonto = this.monto.divide(BigDecimal.valueOf(divisor), scale, roundingMode);
        return new Moneda(nuevoMonto, this.tipoMoneda);
    }
}