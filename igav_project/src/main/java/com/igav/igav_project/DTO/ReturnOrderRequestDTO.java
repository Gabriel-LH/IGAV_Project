package com.igav.igav_project.DTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para procesar la devolución de un contrato de alquiler, liquidar garantías y generar bloqueos (RF-05, RF-08, RF-13).
 *
 * @author IGAV Development Team
 */
public record ReturnOrderRequestDTO(
    Long orderId,
    BigDecimal montoPenalizacionMora,
    List<IncidentRequestDTO> incidencias,
    String updatedBy
) {}
