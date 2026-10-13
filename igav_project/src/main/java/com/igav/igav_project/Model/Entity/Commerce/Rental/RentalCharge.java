package com.igav.igav_project.Model.Entity.Commerce.Rental;

import java.math.BigDecimal;

import com.igav.igav_project.Model.Entity.Commerce.Operation.Operation;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table (name = "rental_charges")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class RentalCharge {

    @Column(name = "tenant_id", insertable = false, updatable = false, nullable = false)
    private Long tenantId;

    @Column(name = "rental_id", insertable = false, updatable = false, nullable = false)
    private Long rentalId;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_item_id", nullable = false)
    private RentalItem rentalItem;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_id", nullable = false)
    private Operation operation;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private RentalChargeType type;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "guarantee_covered_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal guaranteeCoveredAmount;

    @Column(name = "remaining_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal remainingAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RentalChargeStatus status;

    @Embedded
    private AuditMetadata auditMetadata;
}
