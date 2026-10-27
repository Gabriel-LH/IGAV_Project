package com.igav.igav_project.Repository;

import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
}