package com.igav.igav_project.Repository;

import com.igav.igav_project.Model.Entity.Organization.Tenant.TenantPolicy.TenantPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TenantPolicyRepository extends JpaRepository<TenantPolicy, Long> {

    @Query("SELECT policy FROM TenantPolicy policy " +
           "WHERE policy.TenantId.id = :tenantId AND policy.IsActive = true")
    Optional<TenantPolicy> findActiveByTenantId(@Param("tenantId") Long tenantId);
}