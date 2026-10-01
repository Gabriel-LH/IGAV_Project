package com.igav.igav_project.Model.Entity.Organization.Tenant.TenantPolicy;

import jakarta.persistence.Embeddable;

@Embeddable 
public record InventoryPolicy(
    int autoOrderThreshold,
    boolean allowManualAdjustments,
    boolean autoBlockStockIfReserved,
    boolean requireReasonForAdjustment
) {
    public InventoryPolicy() {
        this(5, true, true, true);
    }
}
