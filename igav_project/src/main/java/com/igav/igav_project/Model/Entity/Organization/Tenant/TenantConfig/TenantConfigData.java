package com.igav.igav_project.Model.Entity.Organization.Tenant.TenantConfig;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

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
    List<TransferRoute> transferRoutes,
    int DefaultTransferTime
){}
