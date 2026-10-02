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


    @JoinColumn (name = "module_id", nullable = false)
    @ManyToOne (fetch = FetchType.LAZY)
    private Module Module;
    @Column (name = "plan_feature_key", nullable = false, length = 100)
    @Embedded 
    private PlanFeatureKey PlanFeatureKey;
    @Column (name = "audit_metadata", nullable = true, columnDefinition = "TEXT")
    @Embedded
    private AuditMetadata AuditMetadata;
    
      public ModuleFeature() {
    }

    public ModuleFeature(Long id, Module module,
            PlanFeatureKey planFeatureKey,
            AuditMetadata auditMetadata) {
        this.id = id;
        Module = module;
        PlanFeatureKey = planFeatureKey;
        AuditMetadata = auditMetadata;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Module getModule() {
        return Module;
    }

    public void setModule(Module module) {
        Module = module;
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
