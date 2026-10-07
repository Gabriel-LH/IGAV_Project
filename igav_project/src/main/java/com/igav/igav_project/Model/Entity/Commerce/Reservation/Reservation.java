package com.igav.igav_project.Model.Entity.Commerce.Reservation;

import java.util.Date;

import com.igav.igav_project.Model.Entity.Client.Client;
import com.igav.igav_project.Model.Entity.Commerce.Operation.Operation;
import com.igav.igav_project.Model.Entity.Organization.Branch.Branch;
import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;
import com.nimbusds.openid.connect.sdk.federation.api.OperationType;

import jakarta.persistence.*;

@Entity 
@Table (name = "reservations")
public class Reservation {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name="tenant_id", nullable = false)
    private Tenant tenanId;

    // Cada alquiler específico pertenece a UNA sola Operación
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "operation_id", nullable = false)
    private Operation operation;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "customer_id", nullable = false)
    private Client client;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "branch_id", nullable = false)
    private Branch branch;

    @Column (name = "start_date", nullable = false)
    private Date startDate;
    @Column (name = "end_date", nullable = false)
    private Date endDate;
    
    @Column (name = "hour", nullable = true)
    private String hour;

    @Enumerated (EnumType.STRING)
    @Column (name = "status", nullable = false)
    private ReservationStatus status;

    @Enumerated (EnumType.STRING)
    @Column (name = "operation_type", nullable = false)
    private OperationType operationType;

    @Column (name = "notes", nullable = false)
    private String notes;

    @Embedded 
    @Column (name = "audit_metadata", nullable = false)
    private AuditMetadata auditMetadata;



}
