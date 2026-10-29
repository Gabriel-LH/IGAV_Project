package com.igav.igav_project.Model.Entity.Organization.Tenant.TenantConfig;

import jakarta.persistence.*;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

@Entity 
@Table(name = "tenant_config")
public class TenantConfig {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "loyaltyConfig.enabled", column = @Column(name = "loyalty_enabled")),
        @AttributeOverride(name = "referralConfig.enabled", column = @Column(name = "referral_enabled")),
        @AttributeOverride(name = "updatedAt", column = @Column(name = "config_updated_at")),
        // Agregar aquí cualquier otro 'enabled' de las demás configs si se repiten
    })
    private TenantConfigData tenantConfigData;
    
    @Embedded 
    @AttributeOverrides({
        @AttributeOverride(name = "updatedAt", column = @Column(name = "audit_metadata")),
        // Agregar aquí cualquier otro 'enabled' de las demás configs si se repiten
    })
    private AuditMetadata auditMetadata;


    public TenantConfig(Long id, TenantConfigData tenantConfigData,
            AuditMetadata auditMetadata1) {
        this.id = id;
        this.tenantConfigData = tenantConfigData;
        auditMetadata = auditMetadata1;
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
        return auditMetadata;
    }
    public void setAuditMetadata(AuditMetadata auditMetadata1) {
        auditMetadata = auditMetadata1;
    }

}
