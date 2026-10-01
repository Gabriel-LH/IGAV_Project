package com.igav.igav_project.Model.Entity.Organization.Tenant;

import java.util.List;

import com.igav.igav_project.Model.Entity.Organization.Subscription.Plan.PlanFeature.PlanFeature;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import com.nimbusds.jose.shaded.gson.JsonObject;

import jakarta.persistence.*;

@Entity 
@Table (name = "tenants")
public class Tenant {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "name", nullable = false, length = 100)
    public String Nombre;
    
    @Column (name = "slug", nullable = false, length = 100)
    public String Slug;
    @Column (name = "owner_id", nullable = false, length = 100)
    public String OwnerId;
    @Column (name = "metadata", nullable = true, columnDefinition = "TEXT")
    public JsonObject Metadata = new JsonObject();
    @Column (name = "tenant_config", nullable = true, columnDefinition = "TEXT")
    public JsonObject TenantConfig = new JsonObject();
    @Embedded
    public AuditMetadata AuditMetadata; // O dividir en strings
    @Enumerated(EnumType.STRING)
    @Column (name = "tenant_status", nullable = false, length = 50)
    public TenantStatus TenantStatus;
    
    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanFeature> planFeatures;

    public Tenant(String nombre, String slug, String ownerId, JsonObject metadata, JsonObject tenantConfig,
            AuditMetadata auditMetadata, TenantStatus tenantStatus) {
        Nombre = nombre;
        Slug = slug;
        OwnerId = ownerId;
        Metadata = metadata;
        TenantConfig = tenantConfig;
        AuditMetadata = auditMetadata;
        TenantStatus = tenantStatus;
    }

    public Long getId() {
        return id;
    }
    public String getNombre() {
        return Nombre;
    }
    public String getSlug() {
        return Slug;
    }
    public String getOwnerId() {
        return OwnerId;
    }
    public JsonObject getMetadata() {
        return Metadata;
    }
    public JsonObject getTenantConfig() {
        return TenantConfig;
    }
    public AuditMetadata getAuditMetadata() {
        return AuditMetadata;
    }
    public TenantStatus getTenantStatus() {
        return TenantStatus;
    }

}
