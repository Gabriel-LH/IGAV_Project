package com.igav.igav_project.Model.Entity.Commerce.Guarantee;

import com.igav.igav_project.Model.Entity.Commerce.Operation.Operation;
import com.igav.igav_project.Model.Entity.Commerce.Rental.Rental;
import com.igav.igav_project.Model.Entity.Organization.Branch.Branch;
import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;

@Entity
@Table (name = "guaranties")
public class Guarantee {


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
    @JoinColumn (name ="branch_id", nullable = false)
    private Branch branch;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name ="renatal_id", nullable = false)
    private Rental rental;

    @Enumerated (EnumType.STRING)
    @Column (name = "guarantee_type", nullable = false)
    private GuaranteeType guaranteeType;

    @Column (name = "value", nullable = false)
    private String value;

    @Column (name = "description", nullable = false)
    private String description;

    @Enumerated (EnumType.STRING)
    @Column (name = "status", nullable = false)
    private GuaranteeStatus guaranteeStatus;

    @Column (name = "received_by_id", nullable = true )
    private  String receivedById;

    @Column (name = "returned_by_id", nullable = true)
    private String returnedById;

    @Embedded 
    private AuditMetadata auditMetadata;
}
