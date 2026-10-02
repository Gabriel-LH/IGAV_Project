package com.igav.igav_project.Model.Shared.ValueObjects;

public record NombrePersona(
    TipoPersona tipo,
    String nombres,
    String apellidos,
    String razonSocial
) {
    // Propiedad calculada (en Java se define como un método)
    public String nombreCompleto() {
        if (tipo == TipoPersona.NATURAL) {
            String n = nombres != null ? nombres : "";
            String a = apellidos != null ? apellidos : "";
            return (n + " " + a).trim();
        }
        return razonSocial != null ? razonSocial : "";
    }

    // Método de fábrica para Persona Natural
    public static NombrePersona natural(String nombres, String apellidos) {
        if (nombres == null || nombres.isBlank() || apellidos == null || apellidos.isBlank()) {
            throw new IllegalArgumentException("Nombres y apellidos son requeridos para personas naturales.");
        }

        return new NombrePersona(TipoPersona.NATURAL, nombres.trim(), apellidos.trim(), null);
    }

    // Método de fábrica para Persona Jurídica
    public static NombrePersona juridica(String razonSocial) {
        if (razonSocial == null || razonSocial.isBlank()) {
            throw new IllegalArgumentException("La razón social es requerida para personas jurídicas.");
        }

        return new NombrePersona(TipoPersona.JURIDICA, null, null, razonSocial.trim());
    }
}