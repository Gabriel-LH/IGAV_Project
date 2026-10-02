package com.igav.igav_project.DTO;

import com.igav.igav_project.Model.Entity.Order.OrderItemType;
import java.math.BigDecimal;

/**
 * DTO para la respuesta del detalle de una prenda en un contrato u orden.
 *
 * @author IGAV Development Team
 */
public record OrderItemResponseDTO(
    Long id,
    Long garmentId,
    String garmentName,
    String garmentSku,
    OrderItemType tipoItem,
    BigDecimal precioAplicado,
    BigDecimal garantiaAplicada
) {}