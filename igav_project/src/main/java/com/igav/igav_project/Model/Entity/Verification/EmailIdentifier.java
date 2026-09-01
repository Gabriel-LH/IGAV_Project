package com.igav.igav_project.Model.Entity.Verification;

public record EmailIdentifier(String value) {

    public EmailIdentifier {
        if(value.isEmpty() || value.contains("@") ){
            throw new IllegalArgumentException("El identificador debe ser un correo válido.");
        }
    }
}