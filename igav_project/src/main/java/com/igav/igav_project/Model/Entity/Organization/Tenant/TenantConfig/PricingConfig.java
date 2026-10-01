package com.igav.igav_project.Model.Entity.Organization.Tenant.TenantConfig;

import jakarta.persistence.Embeddable;

@Embeddable 
public record PricingConfig(
    int pricePrecision,
    double maxDiscountLimit,
    boolean allowNegativeStock,
    boolean allowDiscountStacking,
    double highDiscountThreshold,
    boolean requirePinForHighDiscount
) {

}
