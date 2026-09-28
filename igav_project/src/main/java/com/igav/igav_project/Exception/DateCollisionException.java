package com.igav.igav_project.Exception;

/**
 * Excepción lanzada al detectar solapamiento o cruce de fechas en la reserva de una prenda (RF-04).
 *
 * @author IGAV Development Team
 */
public class DateCollisionException extends RuntimeException {
    public DateCollisionException(String message) {
        super(message);
    }
}
