package com.igav.igav_project.Model.Entity.Organization.Tenant.TenantConfig;

public record TransferRoute(
    String id,
    String status,
    String originBranchId,
    int estimatedTimeHours,
    String destinationBranchId
) {

}
