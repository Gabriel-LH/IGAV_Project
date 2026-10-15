package com.igav.igav_project.Model.Entity.Inventory.Transfer.TransferRoute;

import com.igav.igav_project.Model.Entity.Organization.Branch.Branch;
import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table (name = "transfer_routes")
@Getter 
@Setter 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class TransferRoute {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "origin_branch_id", nullable = false)
    private Branch originBranch;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destination_branch_id", nullable = false)
    private Branch destinationBranch;

    @Column(name = "estimated_time_hours", nullable = false)
    private int estimatedTimeHours;

    @Enumerated(EnumType.STRING)
    @Column(name = "transfer_route_status", nullable = false)
    private TransferRouteStatus transferRouteStatus;

    @Builder.Default
    @Column(name = "metadata", columnDefinition = "TEXT", nullable = false)
    private String metadata = "{}";

    @Embedded
    private AuditMetadata auditMetadata;

}
