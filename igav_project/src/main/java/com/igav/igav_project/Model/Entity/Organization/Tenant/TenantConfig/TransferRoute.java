package com.igav.igav_project.Model.Entity.Organization.Tenant.TenantConfig;

import jakarta.persistence.Embeddable;

@Embeddable
public record TransferRoute(
    String id,
    String status,
    String originBranchId,
    int estimatedTimeHours,
    String destinationBranchId
) {

}
