package com.igav.igav_project.Model.Entity.Organization.Tenant.TenantConfig;

import jakarta.persistence.Embeddable;

@Embeddable 
public record LoyaltyConfig(
    boolean enabled,
    double earnRate,
    double redemptionValue,
    int minPointsToRedeem
) {

}
