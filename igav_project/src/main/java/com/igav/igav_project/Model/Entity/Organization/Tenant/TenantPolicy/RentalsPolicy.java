package com.igav.igav_project.Model.Entity.Organization.Tenant.TenantPolicy;

import jakarta.persistence.Embeddable;

@Embeddable 
public record RentalsPolicy(
    boolean allowLateReturn,
    int lateToleranceHours,
    LateFeeType lateFeeType,
    double lateFeeValue,
    int defaultRentalDurationDays,
    int minRentalDurationDays,
    boolean requireGuarantee,
    boolean inclusiveDayCalculation,
    boolean chargePickupDay,
    boolean chargeReturnDay
) {

    public RentalsPolicy() {
        this(true, 2,  LateFeeType.FIXED, 0, 3, 1, true, true, true, true);
    }
}
