package com.igav.igav_project.DTO;

import com.igav.igav_project.Model.Entity.Commerce.Rental.ReturnCondition;
import java.math.BigDecimal;

/**
 * DTO para especificar una incidencia observada en la devolución de una prenda.
 *
 * @author IGAV Development Team
 */
public record RentalReturnItemRequestDTO(
    Long inventoryItemId,
    ReturnCondition condicionRecibida,
    BigDecimal cargoDanio,
    String descripcion
) {}
