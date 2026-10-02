package com.igav.igav_project.Model.Entity.Organization.Tenant.TenantConfig;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable 
public record TaxConfig(
    double rate,
    @Embedded 
    RoundingConfig roundingConfig,
    String calculationMode
) {

}
