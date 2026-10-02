package com.igav.igav_project.Model.Entity.Organization.Subscription.TenantModule;

import java.util.Date;

import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;

import jakarta.persistence.*;

@Entity 
@Table(name = "tenant_modules")
public class TenantModule {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn (name = "tenant_id", nullable = false)
    @ManyToOne (fetch = FetchType.LAZY)
    private Tenant tenant;
    @JoinColumn (name = "module_id", nullable = false)
    @ManyToOne (fetch = FetchType.LAZY)
    private Module module;
    @Column (name = "status", nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private TenandModuleStatus status;
    private Date startDate;
    private Date endDate;

    public TenantModule() {
    }

    public TenantModule(Long id, Tenant tenant, Module module, TenandModuleStatus status, Date startDate,
            Date endDate) {
        this.id = id;
        this.tenant = tenant;
        this.module = module;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public TenandModuleStatus getStatus() {
        return status;
    }

    public void setStatus(TenandModuleStatus status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
