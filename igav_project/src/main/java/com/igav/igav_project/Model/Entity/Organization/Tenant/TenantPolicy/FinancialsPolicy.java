package com.igav.igav_project.Model.Entity.Organization.Tenant.TenantPolicy;

import jakarta.persistence.Embeddable;

@Embeddable 
public record FinancialsPolicy(
    boolean allowInstallments,
    double maxCreditPerClient,
    boolean allowNegativeBalance,
    boolean autoApplyChargesOnDamage
) {
    public FinancialsPolicy() {
        this(false, 0, false, true);
    }
}
