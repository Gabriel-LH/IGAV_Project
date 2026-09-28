package com.igav.igav_project.DTO;

import com.igav.igav_project.Model.Entity.Maintenance.IncidentType;
import java.math.BigDecimal;

/**
 * DTO para especificar una incidencia observada en la devolución de una prenda.
 *
 * @author IGAV Development Team
 */
public record IncidentRequestDTO(
    Long garmentId,
    IncidentType tipoIncidencia,
    BigDecimal montoDescuentoGarantia,
    String descripcion
) {}
