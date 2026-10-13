package com.igav.igav_project.Model.Entity.Commerce.Sale;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.igav.igav_project.Model.Entity.Commerce.Operation.CustomerMode;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;

public class Sale {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "operation_id", nullable = false)
    private UUID operationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_mode", nullable = false)
    private CustomerMode customerMode;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "branch_id", nullable = false)
    private UUID branchId;

    @Column(name = "seller_id")
    private String sellerId;

    @Column(name = "reservation_id", nullable = false)
    private UUID reservationId;

    @Column(name = "total_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "sub_total_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal subTotalAmount;

    @Column(name = "total_discount", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalDiscount;

    @Column(name = "sale_date", nullable = false)
    private LocalDateTime saleDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "out_date")
    private LocalDateTime outDate;

    @Column(name = "real_out_date_time")
    private LocalDateTime realOutDateTime;

    @Column(name = "canceled_date")
    private LocalDateTime canceledDate;

    @Column(name = "return_date_time")
    private LocalDateTime returnDateTime;

    @Column(name = "amount_refunded", precision = 12, scale = 2, nullable = false)
    private BigDecimal amountRefunded;

    @Embedded
    private AuditMetadata auditMetadata;
}
