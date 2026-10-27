package com.igav.igav_project.Model.Entity.Organization.Subscription.Plan.PlanFeature;

import com.igav.igav_project.Model.Entity.Organization.Subscription.Plan.Plan;
import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;

import jakarta.persistence.*;

@Entity 
@Table(name = "plan_features")
public class PlanFeature {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)    
    private Tenant Tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "plan_id", nullable = false)
    private Plan Plan;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "plan_feature_key", nullable = false, length = 100)
    private PlanFeatureKey PlanFeatureKey;
    
    public PlanFeature() {
    }
    
    public PlanFeature(
        Long id, 
        Tenant tenant,
        Plan plan,
        PlanFeatureKey planFeatureKey) {
        this.id = id;
        Tenant = tenant;
        Plan = plan;
        PlanFeatureKey = planFeatureKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tenant getTenant() {
        return Tenant;
    }

    public void setTenant(Tenant tenant) {
        Tenant = tenant;
    }

    public Plan getPlan() {
        return Plan;
    }

    public void setPlan(Plan plan) {
        Plan = plan;
    }

    public PlanFeatureKey getPlanFeatureKey() {
        return PlanFeatureKey;
    }

    public void setPlanFeatureKey(PlanFeatureKey planFeatureKey) {
        PlanFeatureKey = planFeatureKey;
    }

  
}
