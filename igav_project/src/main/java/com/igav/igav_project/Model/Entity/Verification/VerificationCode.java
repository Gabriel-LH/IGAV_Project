package com.igav.igav_project.Model.Entity.Verification;

import jakarta.persistence.Embeddable;
import java.util.concurrent.ThreadLocalRandom;

@Embeddable
public record VerificationCode(String value) {

    // Constructor compacto: se ejecuta siempre que se intente crear o reconstruir el objeto
    public VerificationCode {
        if (value == null || value.isBlank() || value.length() != 6 || !value.matches("\\d+")) {
            throw new IllegalArgumentException("El código de verificación debe tener exactamente 6 dígitos numéricos.");
        }
    }

    // Método para generar un OTP aleatorio de 6 dígitos
    public static VerificationCode generate() {
        int randomCode = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return new VerificationCode(String.valueOf(randomCode));
    }

    // Método para reconstruir el código (por ejemplo, al leerlo de la base de datos)
    public static VerificationCode create(String value) {
        return new VerificationCode(value);
    }
}
