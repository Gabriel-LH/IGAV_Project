package com.igav.igav_project.Model.Entity.Organization.Tenant.TenantConfig;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.Embeddable;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Embedded;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.JoinColumn;

@Embeddable 
public record TenantConfigData(
    @Embedded 
    TaxConfig taxConfig,
    @Embedded 
    CashConfig cashConfig,
    @Embedded 
    LoyaltyConfig loyaltyConfig,
    @Embedded 
    PricingConfig pricingConfig,
    String Currency,
    @Embedded 
    DiscountConfig discountConfig,
    @Embedded 
    ReferralConfig referralConfig,
    Date updatedAt,
    @ElementCollection
    @CollectionTable(name = "tenant_config_transfer_routes", joinColumns = @JoinColumn(name = "tenant_config_id"))
    List<TransferRoute> transferRoutes,
    int DefaultTransferTime
){}
