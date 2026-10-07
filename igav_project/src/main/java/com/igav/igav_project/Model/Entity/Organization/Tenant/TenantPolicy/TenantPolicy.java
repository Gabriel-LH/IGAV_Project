package com.igav.igav_project.Model.Entity.Organization.Tenant.TenantPolicy;

import jakarta.persistence.*;

import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;
import com.igav.igav_project.Model.Shared.ValueObjects.*;


@Entity 
@Table (name = "tenant_policies")
public class TenantPolicy {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn (name = "tenant_id", nullable = false)
    @ManyToOne (fetch = FetchType.LAZY)
    private Tenant TenantId;
    @Column (name = "version", nullable = false)
    private int Version;
    @Column (name = "is_active", nullable = false)
    private boolean IsActive;
    @Embedded
    @Column (name = "audit_metadata", nullable = false, length = 100)
    private AuditMetadata AuditMetadata;
    @Column (name = "sales_policy", nullable = false)
    @Embedded
    private SalesPolicy SalesPolicy;
    @Column (name = "rentals_policy", nullable = false)
    @Embedded
    private RentalsPolicy RentalsPolicy;
    @Column (name = "reservations_policy", nullable = false)
    @Embedded 
    private ReservationsPolicy ReservationsPolicy;
    @Column (name = "securities_policy", nullable = false)
    @Embedded 
    private SecuritiesPolicy SecuritiesPolicy;
    @Column (name = "inventory_policy", nullable = false)
    @Embedded
    private InventoryPolicy InventoryPolicy;
    @Column (name = "financials_policy", nullable = false)
    @Embedded
    private FinancialsPolicy FinancialsPolicy;


    public TenantPolicy() {
    }

    public TenantPolicy(Long id, Tenant tenantId, int version, boolean isActive,
        AuditMetadata auditMetadata,
        SalesPolicy salesPolicy,
            RentalsPolicy rentalsPolicy,
            ReservationsPolicy reservationsPolicy,
            SecuritiesPolicy securitiesPolicy,
            InventoryPolicy inventoryPolicy,
            FinancialsPolicy financialsPolicy) {
        this.id = id;
        TenantId = tenantId;
        Version = version;
        IsActive = isActive;
        AuditMetadata = auditMetadata;
        SalesPolicy = salesPolicy;
        RentalsPolicy = rentalsPolicy;
        ReservationsPolicy = reservationsPolicy;
        SecuritiesPolicy = securitiesPolicy;
        InventoryPolicy = inventoryPolicy;
        FinancialsPolicy = financialsPolicy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tenant getTenantId() {
        return TenantId;
    }

    public void setTenantId(Tenant tenantId) {
        TenantId = tenantId;
    }

    public int getVersion() {
        return Version;
    }

    public void setVersion(int version) {
        Version = version;
    }

    public boolean isIsActive() {
        return IsActive;
    }

    public void setIsActive(boolean isActive) {
        IsActive = isActive;
    }

    public AuditMetadata getAuditMetadata() {
        return AuditMetadata;
    }

    public void setAuditMetadata(AuditMetadata auditMetadata) {
        AuditMetadata = auditMetadata;
    }

    public SalesPolicy getSalesPolicy() {
        return SalesPolicy;
    }

    public void setSalesPolicy(SalesPolicy salesPolicy) {
        SalesPolicy = salesPolicy;
    }

    public RentalsPolicy getRentalsPolicy() {
        return RentalsPolicy;
    }

    public void setRentalsPolicy(RentalsPolicy rentalsPolicy) {
        RentalsPolicy = rentalsPolicy;
    }

    public ReservationsPolicy getReservationsPolicy() {
        return ReservationsPolicy;
    }

    public void setReservationsPolicy(ReservationsPolicy reservationsPolicy) {
        ReservationsPolicy = reservationsPolicy;
    }

    public SecuritiesPolicy getSecuritiesPolicy() {
        return SecuritiesPolicy;
    }

    public void setSecuritiesPolicy(SecuritiesPolicy securitiesPolicy) {
        SecuritiesPolicy = securitiesPolicy;
    }

    public InventoryPolicy getInventoryPolicy() {
        return InventoryPolicy;
    }

    public void setInventoryPolicy(InventoryPolicy inventoryPolicy) {
        InventoryPolicy = inventoryPolicy;
    }

    public FinancialsPolicy getFinancialsPolicy() {
        return FinancialsPolicy;
    }

    public void setFinancialsPolicy(FinancialsPolicy financialsPolicy) {
        FinancialsPolicy = financialsPolicy;
    }

    
}
