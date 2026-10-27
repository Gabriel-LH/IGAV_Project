package com.igav.igav_project.Model.Entity.Organization.Tenant;

import java.util.ArrayList;
import java.util.List;

import com.igav.igav_project.Model.Entity.Catalog.Attribute.AttributeType.AttributeType;
import com.igav.igav_project.Model.Entity.Catalog.Brand.Brand;
import com.igav.igav_project.Model.Entity.Commerce.CashSession.CashSession;
import com.igav.igav_project.Model.Entity.Commerce.Guarantee.Guarantee;
import com.igav.igav_project.Model.Entity.Commerce.Operation.Operation;
import com.igav.igav_project.Model.Entity.Commerce.Payment.Payment;
import com.igav.igav_project.Model.Entity.Commerce.Payment.PaymentMethod.PaymentMethod;
import com.igav.igav_project.Model.Entity.Commerce.Rental.Rental;
import com.igav.igav_project.Model.Entity.Commerce.Reservation.Reservation;
import com.igav.igav_project.Model.Entity.Commerce.Sale.Sale;
import com.igav.igav_project.Model.Entity.Inventory.InventoryItem.InventoryItem;
import com.igav.igav_project.Model.Entity.Inventory.Movement.InventoryMovement;
import com.igav.igav_project.Model.Entity.Inventory.ProductVariantStock.ProductVariantStock;
import com.igav.igav_project.Model.Entity.Inventory.StockAdjustment.StockAdjustment;
import com.igav.igav_project.Model.Entity.Inventory.Transfer.Transfer;
import com.igav.igav_project.Model.Entity.Organization.Branch.Branch;
import com.igav.igav_project.Model.Entity.Organization.Subscription.Module.Module;
import com.igav.igav_project.Model.Entity.Organization.Subscription.TenantModule.TenantModule;
import com.igav.igav_project.Model.Entity.Organization.Subscription.TenantSubscription.TenantSubscription;
import com.igav.igav_project.Model.Entity.RecursosHumanos.Employee.Employee;
import com.igav.igav_project.Model.Entity.RecursosHumanos.MemberShip.UserTenantMemberShip;
import com.igav.igav_project.Model.Entity.RecursosHumanos.Role.Permission;
import com.igav.igav_project.Model.Entity.RecursosHumanos.Role.Role;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;
import com.igav.igav_project.Model.Shared.ValueObjects.DocumentoIdentidad;

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
    public String Metadata = "{}";
    @Column (name = "tenant_config", nullable = true, columnDefinition = "TEXT")
    public String TenantConfig = "{}";
    @Embedded
    public AuditMetadata AuditMetadata; // O dividir en strings
    @Enumerated(EnumType.STRING)
    @Column (name = "tenant_status", nullable = false, length = 50)
    public TenantStatus TenantStatus;

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
    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InventoryItem> inventoryItems = new ArrayList<>();
    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sale> sales = new ArrayList<>();
    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();
    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentMethod> paymentMethods = new ArrayList<>();
    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CashSession> cashSessions = new ArrayList<>();
    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InventoryMovement>inventoryMovements = new ArrayList<>();
    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariantStock> productVariantStocks = new ArrayList<>();
    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transfer> transfers = new ArrayList<>();
    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockAdjustment> stockAdjustments = new ArrayList<>();
    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees = new ArrayList<>();
    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTenantMemberShip> userTenantMemberShips = new ArrayList<>();
    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Role> roles = new ArrayList<>();
    @OneToMany (mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Permission> permissions = new ArrayList<>();





    public Tenant(String nombre, String slug, String ownerId, String metadata, String tenantConfig,
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
    public String getMetadata() {
        return Metadata;
    }
    public String getTenantConfig() {
        return TenantConfig;
    }
    public AuditMetadata getAuditMetadata() {
        return AuditMetadata;
    }
    public TenantStatus getTenantStatus() {
        return TenantStatus;
    }

}
