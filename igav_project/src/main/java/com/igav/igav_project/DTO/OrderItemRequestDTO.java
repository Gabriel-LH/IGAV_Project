package com.igav.igav_project.DTO;

import com.igav.igav_project.Model.Entity.Order.OrderItemType;
import java.math.BigDecimal;

/**
 * DTO para la especificación del detalle de prendas en una orden.
 *
 * @author IGAV Development Team
 */
public record OrderItemRequestDTO(
    Long garmentId,
    OrderItemType tipoItem,
    BigDecimal precioAplicado,
    BigDecimal garantiaAplicada
) {}
