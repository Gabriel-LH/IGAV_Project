package com.igav.igav_project.Model.Entity.Commerce.Rental;

import com.igav.igav_project.Model.Entity.Inventory.InventoryItem.InventoryItem;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rental_return_inspections", uniqueConstraints = {
        @UniqueConstraint(name = "uk_return_inspection_rental_item", columnNames = "rental_item_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalReturnInspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_item_id", nullable = false, unique = true)
    private RentalItem rentalItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id", nullable = false)
    private InventoryItem inventoryItem;

    @Enumerated(EnumType.STRING)
    @Column(name = "received_condition", nullable = false)
    private ReturnCondition receivedCondition;

    @Enumerated(EnumType.STRING)
    @Column(name = "disposition")
    private ReturnDisposition disposition;

    @Enumerated(EnumType.STRING)
    @Column(name = "guarantee_decision", nullable = false)
    private GuaranteeDecision guaranteeDecision;

    @Column(name = "damage_notes", columnDefinition = "TEXT")
    private String damageNotes;

    @Column(name = "damage_charge", precision = 12, scale = 2, nullable = false)
    private BigDecimal damageCharge;

    @Column(name = "late_fee", precision = 12, scale = 2, nullable = false)
    private BigDecimal lateFee;

    @Column(name = "guarantee_applied", precision = 12, scale = 2, nullable = false)
    private BigDecimal guaranteeApplied;

    @Column(name = "outstanding_balance", precision = 12, scale = 2, nullable = false)
    private BigDecimal outstandingBalance;

    @Column(name = "inspected_at", nullable = false)
    private LocalDateTime inspectedAt;

    @Column(name = "inspected_by", nullable = false)
    private String inspectedBy;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "completed_by")
    private String completedBy;

    @Embedded
    private AuditMetadata auditMetadata;
}