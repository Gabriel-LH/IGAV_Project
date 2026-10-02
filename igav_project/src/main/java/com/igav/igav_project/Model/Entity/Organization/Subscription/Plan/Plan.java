package com.igav.igav_project.Model.Entity.Organization.Subscription.Plan;

import java.sql.Date;
import java.util.List;

import com.igav.igav_project.Model.Entity.Organization.Subscription.Plan.PlanFeature.PlanFeature;
import com.igav.igav_project.Model.Entity.Organization.Subscription.Plan.PlanLimit.PlanLimit;
import com.igav.igav_project.Model.Entity.Organization.Subscription.Plan.PlonModule.PlanModule;
import com.igav.igav_project.Model.Shared.ValueObjects.Moneda;

import jakarta.persistence.*;

@Entity 
@Table (name = "plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "description", length = 255)
    private String description;
    @Column(name = "monthly_price", nullable = false)
    @Embedded 
    private Moneda PrecioMensual;
    @Column(name = "trial_days", nullable = false)
    public int diasDePrueba;
    @Column(name = "annual_price", nullable = false)
    @Embedded 
    private Moneda precioAnual;
    @Column(name = "is_active", nullable = false)
    private boolean isActive;
    @Column(name = "sort_order", nullable = false)
    public int SortOrder;
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @OneToMany (mappedBy = "Plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanModule> planModules;

    @OneToMany (mappedBy = "Plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanLimit> planLimits;

    @OneToMany (mappedBy = "Plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanFeature> planFeatures;

    public Plan() {
    }

    public Plan(Long id, String name, String description, Moneda precioMensual, int diasDePrueba, Moneda precioAnual,
            boolean isActive, int sortOrder, Date createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        PrecioMensual = precioMensual;
        this.diasDePrueba = diasDePrueba;
        this.precioAnual = precioAnual;
        this.isActive = isActive;
        SortOrder = sortOrder;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Moneda getPrecioMensual() {
        return PrecioMensual;
    }

    public void setPrecioMensual(Moneda precioMensual) {
        PrecioMensual = precioMensual;
    }

    public int getDiasDePrueba() {
        return diasDePrueba;
    }

    public void setDiasDePrueba(int diasDePrueba) {
        this.diasDePrueba = diasDePrueba;
    }

    public Moneda getPrecioAnual() {
        return precioAnual;
    }

    public void setPrecioAnual(Moneda precioAnual) {
        this.precioAnual = precioAnual;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getSortOrder() {
        return SortOrder;
    }

    public void setSortOrder(int sortOrder) {
        SortOrder = sortOrder;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
