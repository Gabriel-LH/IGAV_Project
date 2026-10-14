package com.igav.igav_project.Model.Entity.Commerce.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.igav.igav_project.Model.Entity.Commerce.Operation.Operation;
import com.igav.igav_project.Model.Entity.Commerce.Payment.PaymentMethod.PaymentMethod;
import com.igav.igav_project.Model.Entity.Organization.Branch.Branch;
import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table (name = "payments")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class Payment {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_id", nullable = false)
    private Operation operation;

    @ManyToOne 
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "received_by_id", nullable = false)
    private String receivedById;

    @Column(name = "amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_direction", nullable = false)
    private PaymentDirection paymentDirection;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private PaymentCategory category;

    @Column(name = "original_payment_id")
    private Long originalPaymentId;

    @Builder.Default
    @Column(name = "reference", nullable = false)
    private String reference = "";

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Embedded
    private AuditMetadata metadata;

}
