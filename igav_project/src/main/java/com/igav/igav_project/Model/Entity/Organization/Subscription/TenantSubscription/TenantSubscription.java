package com.igav.igav_project.Model.Entity.Organization.Subscription.TenantSubscription;

import java.util.Date;

import com.igav.igav_project.Model.Entity.Organization.Subscription.Plan.Plan;
import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;

@Entity 
@Table (name = "tenant_subscriptions")
public class TenantSubscription {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn (name = "tenant_id", nullable = false)
    @ManyToOne (fetch = FetchType.LAZY) 
    private Tenant tenant;

    @JoinColumn (name = "plan_id", nullable = false)
    @ManyToOne (fetch = FetchType.LAZY)
    private Plan plan;

    @Column (name = "status", nullable = false, length = 100)
    @Enumerated (EnumType.STRING)
    private TenantSubscriptionStatus status;

    @Column (name = "billing_cycle", nullable = false, length = 100)
    @Enumerated (EnumType.STRING)
    private BillingCycle billingCycle;

    @Column (name = "start_date", nullable = false)
    private Date startDate;
    @Column (name = "end_date", nullable = true)
    private Date trialEndDate;
    @Column (name = "current_period_start", nullable = true)
    private Date currentPeriodStart;
    @Column (name = "current_period_end", nullable = true)
    private Date currentPeriodEnd;
    @Column (name = "canceled_at", nullable = true)
    private Date canceledAt;
    @Column (name = "provider", nullable = false, length = 100)
    @Enumerated (EnumType.STRING)
    private Provider provider;
    @Column (name = "external_subscription_id", nullable = true, length = 255)
    private String externalSubscriptionId;

    @Column (name = "metadata", nullable = true, columnDefinition = "TEXT")
    @Embedded 
    private AuditMetadata auditMetadata;

    public TenantSubscription() {
    }

    public TenantSubscription(Long id, Tenant tenant, Plan plan, TenantSubscriptionStatus status,
            BillingCycle billingCycle, Date startDate, Date trialEndDate, Date currentPeriodStart,
            Date currentPeriodEnd, Date canceledAt, Provider provider, String externalSubscriptionId,
            AuditMetadata auditMetadata) {
        this.id = id;
        this.tenant = tenant;
        this.plan = plan;
        this.status = status;
        this.billingCycle = billingCycle;
        this.startDate = startDate;
        this.trialEndDate = trialEndDate;
        this.currentPeriodStart = currentPeriodStart;
        this.currentPeriodEnd = currentPeriodEnd;
        this.canceledAt = canceledAt;
        this.provider = provider;
        this.externalSubscriptionId = externalSubscriptionId;
        this.auditMetadata = auditMetadata;
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

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public TenantSubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(TenantSubscriptionStatus status) {
        this.status = status;
    }

    public BillingCycle getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(BillingCycle billingCycle) {
        this.billingCycle = billingCycle;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getTrialEndDate() {
        return trialEndDate;
    }

    public void setTrialEndDate(Date trialEndDate) {
        this.trialEndDate = trialEndDate;
    }

    public Date getCurrentPeriodStart() {
        return currentPeriodStart;
    }

    public void setCurrentPeriodStart(Date currentPeriodStart) {
        this.currentPeriodStart = currentPeriodStart;
    }

    public Date getCurrentPeriodEnd() {
        return currentPeriodEnd;
    }

    public void setCurrentPeriodEnd(Date currentPeriodEnd) {
        this.currentPeriodEnd = currentPeriodEnd;
    }

    public Date getCanceledAt() {
        return canceledAt;
    }

    public void setCanceledAt(Date canceledAt) {
        this.canceledAt = canceledAt;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getExternalSubscriptionId() {
        return externalSubscriptionId;
    }

    public void setExternalSubscriptionId(String externalSubscriptionId) {
        this.externalSubscriptionId = externalSubscriptionId;
    }

    public AuditMetadata getAuditMetadata() {
        return auditMetadata;
    }

    public void setAuditMetadata(AuditMetadata auditMetadata) {
        this.auditMetadata = auditMetadata;
    }
}
