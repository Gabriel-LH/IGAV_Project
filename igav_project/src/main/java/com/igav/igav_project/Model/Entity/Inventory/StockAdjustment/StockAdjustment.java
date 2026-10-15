package com.igav.igav_project.Model.Entity.Inventory.StockAdjustment;

import com.igav.igav_project.Model.Entity.Organization.Branch.Branch;
import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;
import com.igav.igav_project.Model.Entity.User.User;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table (name = "stock_adjustment")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class StockAdjustment {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false) // <- Ajuste físico realizado en la sucursal
    private Branch branch;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "reference_number", nullable = false)
    private String referenceNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason", nullable = false)
    private StockAdjustmentReason reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StockAdjustmentStatus status;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Embedded
    private AuditMetadata auditMetadata;


}
