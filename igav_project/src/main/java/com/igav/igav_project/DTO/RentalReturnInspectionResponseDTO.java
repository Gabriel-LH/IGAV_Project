package com.igav.igav_project.DTO;

import com.igav.igav_project.Model.Entity.Commerce.Rental.GuaranteeDecision;
import com.igav.igav_project.Model.Entity.Commerce.Rental.ReturnCondition;
import com.igav.igav_project.Model.Entity.Commerce.Rental.ReturnDisposition;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RentalReturnInspectionResponseDTO(
        Long inspectionId,
        Long rentalItemId,
        Long inventoryItemId,
        ReturnCondition receivedCondition,
        ReturnDisposition disposition,
        GuaranteeDecision guaranteeDecision,
        String notes,
        BigDecimal damageCharge,
        BigDecimal lateFee,
        BigDecimal guaranteeApplied,
        BigDecimal outstandingBalance,
        LocalDateTime inspectedAt,
        String inspectedBy,
        LocalDateTime completedAt,
        String completedBy) {
}