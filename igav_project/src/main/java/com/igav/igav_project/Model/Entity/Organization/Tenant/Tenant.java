package com.igav.igav_project.Model.Entity.Organization.Tenant;

import java.util.List;

import com.igav.igav_project.Model.Entity.Catalog.Attribute.AttributeType.AttributeType;
import com.igav.igav_project.Model.Entity.Catalog.Brand.Brand;
import com.igav.igav_project.Model.Entity.Commerce.Guarantee.Guarantee;
import com.igav.igav_project.Model.Entity.Commerce.Operation.Operation;
import com.igav.igav_project.Model.Entity.Commerce.Rental.Rental;
import com.igav.igav_project.Model.Entity.Commerce.Reservation.Reservation;
import com.igav.igav_project.Model.Entity.Organization.Branch.Branch;
import com.igav.igav_project.Model.Entity.Organization.Subscription.Plan.Plan;
import com.igav.igav_project.Model.Entity.Organization.Subscription.TenantModule.TenantModule;
import com.igav.igav_project.Model.Entity.Organization.Subscription.TenantSubscription.TenantSubscription;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;
import com.igav.igav_project.Model.Shared.ValueObjects.DocumentoIdentidad;
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
    @Column(name = "razon_social", nullable = false, length = 150)
    private String razonSocial;

    @Embedded
    @AttributeOverride(name = "numero", column = @Column(name = "ruc_numero", nullable = false, unique = true))
    @AttributeOverride(name = "tipo", column = @Column(name = "ruc_tipo", nullable = false))
    private DocumentoIdentidad ruc;

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

    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Plan> plans;
    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Module> modules;
    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TenantModule> tenantModules;
    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TenantSubscription> tenantSubscriptions;
    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Branch> branchs;
    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Operation> operations;
    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rental> rentals;
    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;
    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Guarantee> guarantees;
    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Brand> brands;
    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttributeType> attributeTypes;





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
