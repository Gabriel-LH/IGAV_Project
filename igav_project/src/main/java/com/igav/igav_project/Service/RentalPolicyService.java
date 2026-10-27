package com.igav.igav_project.Service;

import com.igav.igav_project.Model.Entity.Commerce.Guarantee.Guarantee;
import com.igav.igav_project.Model.Entity.Commerce.Guarantee.GuaranteeStatus;
import com.igav.igav_project.Model.Entity.Commerce.Guarantee.GuaranteeType;
import com.igav.igav_project.Model.Entity.Commerce.Rental.Rental;
import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;
import com.igav.igav_project.Model.Entity.Organization.Tenant.TenantPolicy.LateFeeType;
import com.igav.igav_project.Model.Entity.Organization.Tenant.TenantPolicy.RentalsPolicy;
import com.igav.igav_project.Model.Entity.Organization.Tenant.TenantPolicy.TenantPolicy;
import com.igav.igav_project.Repository.TenantPolicyRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class RentalPolicyService {

    private final TenantPolicyRepository tenantPolicyRepository;

    public RentalPolicyService(TenantPolicyRepository tenantPolicyRepository) {
        this.tenantPolicyRepository = tenantPolicyRepository;
    }

    public RentalsPolicy getPolicy(Tenant tenant) {
        TenantPolicy tenantPolicy = tenantPolicyRepository.findActiveByTenantId(tenant.getId()).orElse(null);
        return tenantPolicy != null && tenantPolicy.getRentalsPolicy() != null
                ? tenantPolicy.getRentalsPolicy()
                : new RentalsPolicy();
    }

    public boolean hasApplicableGuarantee(Rental rental) {
        return rental.getGuarantees().stream().anyMatch(guarantee -> {
            if (guarantee.getGuaranteeType() == GuaranteeType.NoAplica
                    || (guarantee.getGuaranteeStatus() != GuaranteeStatus.Custodia
                            && guarantee.getGuaranteeStatus() != GuaranteeStatus.Pendiente)
                    || guarantee.getValue() == null
                    || guarantee.getValue().isBlank()) {
                return false;
            }
            return guarantee.getGuaranteeType() != GuaranteeType.Dinero
                    || parseGuaranteeValue(guarantee).signum() > 0;
        });
    }

    public BigDecimal calculateLateFee(Rental rental, LocalDateTime returnedAt) {
        RentalsPolicy policy = getPolicy(rental.getTenant());
        long lateHours = Duration.between(rental.getExpectedReturnDate(), returnedAt).toHours();
        long billableHours = lateHours - policy.lateToleranceHours();
        if (billableHours <= 0) {
            return BigDecimal.ZERO;
        }

        long billableDays = (billableHours + 23) / 24;
        BigDecimal rate = BigDecimal.valueOf(policy.lateFeeValue());
        BigDecimal subtotal = rental.getSubTotal().monto();
        if (policy.lateFeeType() == LateFeeType.PERCENTAGEPERDAY) {
            return subtotal.multiply(rate).multiply(BigDecimal.valueOf(billableDays))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }
        if (policy.lateFeeType() == LateFeeType.PERCENTAGEOFTOTALRENTAL) {
            return subtotal.multiply(rate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }
        return rate.multiply(BigDecimal.valueOf(billableDays)).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getMonetaryGuaranteeBalance(Rental rental) {
        BigDecimal balance = BigDecimal.ZERO;
        for (Guarantee guarantee : rental.getGuarantees()) {
            if (guarantee.getGuaranteeType() == GuaranteeType.Dinero
                    && (guarantee.getGuaranteeStatus() == GuaranteeStatus.Custodia
                            || guarantee.getGuaranteeStatus() == GuaranteeStatus.Pendiente)) {
                balance = balance.add(parseGuaranteeValue(guarantee));
            }
        }
        return balance;
    }

    public void updateGuaranteeStatuses(
            Rental rental, BigDecimal amountRetained, boolean hasDamage, String updatedBy) {
        BigDecimal retainedRemaining = amountRetained;
        for (Guarantee guarantee : rental.getGuarantees()) {
            BigDecimal guaranteeAmount = guarantee.getGuaranteeType() == GuaranteeType.Dinero
                    ? parseGuaranteeValue(guarantee)
                    : BigDecimal.ZERO;
            if (retainedRemaining.signum() > 0 && guaranteeAmount.signum() > 0) {
                BigDecimal retained = guaranteeAmount.min(retainedRemaining);
                retainedRemaining = retainedRemaining.subtract(retained);
                guarantee.setGuaranteeStatus(GuaranteeStatus.Retenida);
            } else if (hasDamage && guarantee.getGuaranteeType() != GuaranteeType.Dinero) {
                guarantee.setGuaranteeStatus(GuaranteeStatus.Retenida);
            } else {
                guarantee.setGuaranteeStatus(GuaranteeStatus.Devuelta);
            }
            guarantee.setReturnedById(updatedBy);
        }
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