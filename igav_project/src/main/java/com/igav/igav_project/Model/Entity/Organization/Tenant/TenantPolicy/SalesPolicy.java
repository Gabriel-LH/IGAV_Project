package com.igav.igav_project.Model.Entity.Organization.Tenant.TenantPolicy;

import jakarta.persistence.Embeddable;

@Embeddable 
public record SalesPolicy(
    boolean allowReturns,
    int maxReturnHours,
    int maxCancelHours,
    boolean allowPriceEdit,
    boolean requireReasonForCancel,
    boolean requireOriginalTicket,
    boolean allowPartialReturns
) {
    // Constructor por defecto que replica los valores de C#
    public SalesPolicy() {
        this(true, 72, 24, false, true, true, true);
    }
}