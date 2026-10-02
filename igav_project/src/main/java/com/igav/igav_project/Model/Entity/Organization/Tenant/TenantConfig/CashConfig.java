package com.igav.igav_project.Model.Entity.Organization.Tenant.TenantConfig;

import java.util.List;

import jakarta.persistence.Embeddable;

@Embeddable 
public record CashConfig(
    List<String> paymentMethods,
    boolean allowNegativePayments,
    boolean openingCashRequired,
    boolean requireClosingReport
) {

}
