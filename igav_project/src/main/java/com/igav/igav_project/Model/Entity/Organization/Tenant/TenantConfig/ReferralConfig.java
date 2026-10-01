package com.igav.igav_project.Model.Entity.Organization.Tenant.TenantConfig;

import jakarta.persistence.Embeddable;

@Embeddable 
public record ReferralConfig(
    boolean enabled,
    String rewardType,
    int rewardValue,
    String triggerCondition,
    String couponDiscountType
) {
    
}
