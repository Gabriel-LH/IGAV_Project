package com.igav.igav_project.Model.Entity.Organization.Subscription.Module;

import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;
import com.igav.igav_project.Model.Entity.Organization.Subscription.Plan.PlanFeature.PlanFeatureKey;

import jakarta.persistence.*;

@Entity 
@Table(name = "module_features")
public class ModuleFeature {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "tenant_id", nullable = false, length = 100)
    private String TenantId;
    @Column (name = "module_id", nullable = false, length = 100)
    private String ModuleId;
    @Column (name = "plan_feature_key", nullable = false, length = 100)
    @Embedded 
    private PlanFeatureKey PlanFeatureKey;
    @Column (name = "audit_metadata", nullable = true, columnDefinition = "TEXT")
    @Embedded
    private AuditMetadata AuditMetadata;
    public ModuleFeature(Long id, String tenantId, String moduleId,
            PlanFeatureKey planFeatureKey,
            AuditMetadata auditMetadata) {
        this.id = id;
        TenantId = tenantId;
        ModuleId = moduleId;
        PlanFeatureKey = planFeatureKey;
        AuditMetadata = auditMetadata;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTenantId() {
        return TenantId;
    }
    public void setTenantId(String tenantId) {
        TenantId = tenantId;
    }
    public String getModuleId() {
        return ModuleId;
    }
    public void setModuleId(String moduleId) {
        ModuleId = moduleId;
    }
    public PlanFeatureKey getPlanFeatureKey() {
        return PlanFeatureKey;
    }
    public void setPlanFeatureKey(PlanFeatureKey planFeatureKey) {
        PlanFeatureKey = planFeatureKey;
    }
    public AuditMetadata getAuditMetadata() {
        return AuditMetadata;
    }
    public void setAuditMetadata(AuditMetadata auditMetadata) {
        AuditMetadata = auditMetadata;
    }
}
