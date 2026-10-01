package com.igav.igav_project.Model.Entity.Organization.Tenant.TenantConfig;

import jakarta.persistence.*;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

@Entity 
@Table(name = "tenant_config")
public class TenantConfig {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (name = "tenant_config_data", nullable = false, length = 100)
    @Embedded 
    private TenantConfigData tenantConfigData;
    @Column (name = "audit_metadata", nullable = false, length = 100)
    @Embedded 
    private AuditMetadata AuditMetadata;


    public TenantConfig(Long id, TenantConfigData tenantConfigData,
            AuditMetadata auditMetadata) {
        this.id = id;
        this.tenantConfigData = tenantConfigData;
        AuditMetadata = auditMetadata;
    }


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public TenantConfigData getTenantConfigData() {
        return tenantConfigData;
    }
    public void setTenantConfigData(TenantConfigData tenantConfigData) {
        this.tenantConfigData = tenantConfigData;
    }
    public AuditMetadata getAuditMetadata() {
        return AuditMetadata;
    }
    public void setAuditMetadata(AuditMetadata auditMetadata) {
        AuditMetadata = auditMetadata;
    }

}
