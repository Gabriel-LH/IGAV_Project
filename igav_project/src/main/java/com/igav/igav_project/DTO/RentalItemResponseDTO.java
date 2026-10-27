package com.igav.igav_project.DTO;

import java.math.BigDecimal;
import com.igav.igav_project.Model.Entity.Commerce.Rental.GuaranteeDecision;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalItemStatus;
import com.igav.igav_project.Model.Entity.Commerce.Rental.ReturnCondition;
import com.igav.igav_project.Model.Entity.Commerce.Rental.ReturnDisposition;

/**
 * DTO para la respuesta del detalle de una prenda en un contrato u orden.
 *
 * @author IGAV Development Team
 */
public record RentalItemResponseDTO(
    Long rentalItemId,
    Long inventoryItemId,
    String productName,
    String serialCode,
    RentalItemStatus status,
    ReturnCondition receivedCondition,
    ReturnDisposition disposition,
    BigDecimal damageCharge,
    BigDecimal mora,
    GuaranteeDecision guaranteeDecision,
    BigDecimal garantiaAplicada,
    BigDecimal saldoPendiente
) {}