package com.igav.igav_project.Model.Entity.Organization.Subscription.Plan.PlanModule;

import com.igav.igav_project.Model.Entity.Organization.Subscription.Plan.Plan;

import jakarta.persistence.*;

@Entity 
@Table (name = "plan_modules")
public class PlanModule {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn (name = "plan_id", nullable = false)
    @ManyToOne (fetch = FetchType.LAZY)
    private Plan Plan;

    @Column (name = "modules_by_section", nullable = false, columnDefinition = "TEXT")
    private String ModulesBySection;

    public PlanModule() {
    }

    public PlanModule(Long id, Plan plan, String modulesBySection) {
        this.id = id;
        Plan = plan;
        ModulesBySection = modulesBySection;
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

    public String getModulesBySection() {
        return ModulesBySection;
    }

    public void setModulesBySection(String modulesBySection) {
        ModulesBySection = modulesBySection;
    }

    
}
