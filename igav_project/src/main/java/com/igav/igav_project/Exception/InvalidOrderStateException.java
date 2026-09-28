package com.igav.igav_project.Exception;

/**
 * Excepción lanzada al intentar ejecutar transiciones ilegales en el estado de una orden o contrato.
 *
 * @author IGAV Development Team
 */
public class InvalidOrderStateException extends RuntimeException {
    public InvalidOrderStateException(String message) {
        super(message);
    }
}
