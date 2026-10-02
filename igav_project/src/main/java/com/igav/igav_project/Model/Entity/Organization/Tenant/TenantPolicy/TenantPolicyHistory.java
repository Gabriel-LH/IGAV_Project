package com.igav.igav_project.Model.Entity.Organization.Tenant.TenantPolicy;

import jakarta.persistence.*;
import com.igav.igav_project.Model.Shared.ValueObjects.*;


@Entity 
@Table (name = "tenant_policies_history")
public class TenantPolicyHistory {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String TenantPolicyId;
    private int Version;
    private boolean IsActive;
    @Embedded
    private AuditMetadata AuditMetadata;
    @Embedded
    private SalesPolicy SalesPolicy;
    @Embedded
    private RentalsPolicy RentalsPolicy;
    @Embedded 
    private ReservationsPolicy ReservationsPolicy;
    @Embedded 
    private SecuritiesPolicy SecuritiesPolicy;
    @Embedded
    private InventoryPolicy InventoryPolicy;
    @Embedded
    private FinancialsPolicy FinancialsPolicy;
    public TenantPolicyHistory(Long id, String tenantPolicyId, int version, boolean isActive,
            AuditMetadata auditMetadata,
            SalesPolicy salesPolicy,
            RentalsPolicy rentalsPolicy,
            ReservationsPolicy reservationsPolicy,
            SecuritiesPolicy securitiesPolicy,
            InventoryPolicy inventoryPolicy,
            FinancialsPolicy financialsPolicy) {
        this.id = id;
        TenantPolicyId = tenantPolicyId;
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
    public String getTenantPolicyId() {
        return TenantPolicyId;
    }
    public void setTenantPolicyId(String tenantPolicyId) {
        TenantPolicyId = tenantPolicyId;
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
