package com.igav.igav_project.Model.Entity.Commerce.CashSession;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.igav.igav_project.Model.Entity.Organization.Branch.Branch;
import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table (name = "cash_sessions")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class CashSession {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "opened_by_id")
    private String openedById;

    @Column(name = "closed_by_id", nullable = false)
    private String closedById;

    @Column(name = "opened_at", nullable = false)
    private LocalDateTime openedAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @Column(name = "session_number", nullable = false)
    private String sessionNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CashSessionStatus status;

    @Column(name = "opened_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal openedAmount;

    @Column(name = "closing_expected_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal closingExpectedAmount;

    @Column(name = "closing_counted_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal closingCountedAmount;

    @Column(name = "closing_difference", precision = 12, scale = 2, nullable = false)
    private BigDecimal closingDifference;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

}
