package com.igav.igav_project.Exception;

/**
 * Excepción lanzada cuando una entidad o recurso solicitado no existe en la base de datos.
 *
 * @author IGAV Development Team
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
