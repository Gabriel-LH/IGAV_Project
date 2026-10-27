package com.igav.igav_project.Service;

import com.igav.igav_project.DTO.RentalItemResponseDTO;
import com.igav.igav_project.DTO.RentalResponseDTO;
import com.igav.igav_project.Model.Entity.Client.Client;
import com.igav.igav_project.Model.Entity.Commerce.Guarantee.Guarantee;
import com.igav.igav_project.Model.Entity.Commerce.Guarantee.GuaranteeType;
import com.igav.igav_project.Model.Entity.Commerce.Rental.Rental;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalCharge;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalChargeType;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalItem;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalReturnInspection;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class RentalResponseMapper {

    public RentalResponseDTO toResponse(Rental rental) {
        BigDecimal guaranteeTotal = BigDecimal.ZERO;
        for (Guarantee guarantee : rental.getGuarantees()) {
            if (guarantee.getGuaranteeType() == GuaranteeType.Dinero) {
                guaranteeTotal = guaranteeTotal.add(parseGuaranteeValue(guarantee));
            }
        }

        BigDecimal coveredTotal = BigDecimal.ZERO;
        BigDecimal lateFee = BigDecimal.ZERO;
        BigDecimal allCharges = BigDecimal.ZERO;
        for (RentalItem item : rental.getRentalItems()) {
            for (RentalCharge charge : item.getRentalCharges()) {
                coveredTotal = coveredTotal.add(charge.getGuaranteeCoveredAmount());
                allCharges = allCharges.add(charge.getAmount());
                if (charge.getType() == RentalChargeType.TarifaRetraso) {
                    lateFee = lateFee.add(charge.getAmount());
                }
            }
        }

        BigDecimal subtotal = rental.getSubTotal() != null ? rental.getSubTotal().monto() : BigDecimal.ZERO;
        BigDecimal discount = rental.getTotalDiscount() != null
                ? rental.getTotalDiscount().monto()
                : BigDecimal.ZERO;
        Client client = rental.getClient();
        List<RentalItemResponseDTO> items = new ArrayList<>();
        for (RentalItem item : rental.getRentalItems()) {
            RentalReturnInspection inspection = item.getReturnInspection();
            items.add(new RentalItemResponseDTO(
                    item.getId(),
                    item.getInventoryItem().getId(),
                    item.getProductName(),
                    item.getSerialCode(),
                    item.getStatus(),
                    inspection != null ? inspection.getReceivedCondition() : null,
                    inspection != null ? inspection.getDisposition() : null,
                    inspection != null ? inspection.getDamageCharge() : BigDecimal.ZERO,
                    inspection != null ? inspection.getLateFee() : BigDecimal.ZERO,
                    inspection != null ? inspection.getGuaranteeDecision() : null,
                    inspection != null ? inspection.getGuaranteeApplied() : BigDecimal.ZERO,
                    inspection != null ? inspection.getOutstandingBalance() : BigDecimal.ZERO));
        }

        return new RentalResponseDTO(
                rental.getId(),
                "ALQ-%06d".formatted(rental.getId()),
                client.getId(),
                client.getNombrePersona().nombreCompleto(),
                rental.getBranch().getId(),
                rental.getOperation().getOperationType().name(),
                rental.getOutDate(),
                rental.getExpectedReturnDate(),
                rental.getActualReturnDate(),
                subtotal,
                discount,
                guaranteeTotal,
                coveredTotal,
                lateFee,
                subtotal.subtract(discount).max(BigDecimal.ZERO).add(allCharges),
                guaranteeTotal.subtract(coveredTotal).max(BigDecimal.ZERO),
                rental.getStatus(),
                items);
    }

    private BigDecimal parseGuaranteeValue(Guarantee guarantee) {
        try {
            return new BigDecimal(guarantee.getValue().trim()).max(BigDecimal.ZERO);
        } catch (RuntimeException exception) {
            throw new IllegalStateException("La garantía monetaria " + guarantee.getId()
                    + " tiene un valor que no es numérico.", exception);
        }
    }
}