package com.igav.igav_project.Model.Entity.Organization.Subscription.Plan.PlonModule;

import java.util.Dictionary;
import java.util.List;

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
    private Dictionary<String, List<PlanModuleKey>> ModulesBySection;

    public PlanModule() {
    }

    public PlanModule(Long id, Plan plan, Dictionary<String, List<PlanModuleKey>> modulesBySection) {
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

    public Dictionary<String, List<PlanModuleKey>> getModulesBySection() {
        return ModulesBySection;
    }

    public void setModulesBySection(Dictionary<String, List<PlanModuleKey>> modulesBySection) {
        ModulesBySection = modulesBySection;
    }

    
}
