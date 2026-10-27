package com.igav.igav_project.DTO;

import java.time.LocalDateTime;

/**
 * DTO para la creación de solicitudes de contratos de alquiler u órdenes de venta (RF-04, RF-06).
 *
 * @author IGAV Development Team
 */
public record CreateRentalRequestDTO(
    Long reservationId,
    LocalDateTime fechaEntrega,
    String observaciones,
    String createdBy
) {}
