package com.igav.igav_project.Model.Entity.Organization.Tenant.TenantPolicy;

import jakarta.persistence.Embeddable;

@Embeddable 
public record SecuritiesPolicy(
    double highDiscountThreshold,
    boolean RequirePinForHighDiscount,
    boolean RequireManagerApprovalForVoid
) {

    public SecuritiesPolicy() {
        this(20, true, true);
    }
}
