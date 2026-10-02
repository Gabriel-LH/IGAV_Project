package com.igav.igav_project.Model.Entity.Organization.Subscription.Plan.PlanLimit;

import com.igav.igav_project.Model.Entity.Organization.Subscription.Plan.Plan;

import jakarta.persistence.*;

@Entity 
@Table(name = "plan_limits")
public class PlanLimit {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn (name = "plan_id", nullable = false)
    @ManyToOne (fetch = FetchType.LAZY)
    private Plan Plan;

    @Column (name = "limit_key", nullable = false, length = 100)
    private PlanLimitKey PlanLimitKey;

    @Column (name = "limit", nullable = false)
    private int Limit;

    public PlanLimit() {
    }

    public PlanLimit(Long id, Plan plan, PlanLimitKey planLimitKey, int limit) {
        this.id = id;
        Plan = plan;
        PlanLimitKey = planLimitKey;
        Limit = limit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Plan getPlan() {
        return Plan;
    }

    public void setPlan(Plan plan) {
        Plan = plan;
    }

    public PlanLimitKey getPlanLimitKey() {
        return PlanLimitKey;
    }

    public void setPlanLimitKey(PlanLimitKey planLimitKey) {
        PlanLimitKey = planLimitKey;
    }

    public int getLimit() {
        return Limit;
    }

    public void setLimit(int limit) {
        Limit = limit;
    }

    
}
