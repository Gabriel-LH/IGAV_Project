package com.igav.igav_project.Model.Entity.Organization.Tenant.TenantConfig;

import jakarta.persistence.Embeddable;

@Embeddable 
public record DiscountConfig(
    boolean allowStacking,
    double maxPercentageAllowed,
    double requireAdminAuthover
) {

}
