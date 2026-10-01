package com.igav.igav_project.Model.Entity.Organization.Tenant.TenantPolicy;

import jakarta.persistence.Embeddable;

@Embeddable 
public record ReservationsPolicy(
     boolean autoExpireReservations,
    boolean requireDownPayment,
    double depositPercentage,
    int expiresAfterHours,
    int minDownPaymentPercentage
) {
    public ReservationsPolicy() {
        this(true, false, 50, 24, 0);
    }
}


