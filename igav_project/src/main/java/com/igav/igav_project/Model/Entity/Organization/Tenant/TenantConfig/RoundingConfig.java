package com.igav.igav_project.Model.Entity.Organization.Tenant.TenantConfig;

import jakarta.persistence.Embeddable;

@Embeddable 
public record RoundingConfig(
    String ApplyOn,
    double RoundTo,
    String Strategy
) {

}
