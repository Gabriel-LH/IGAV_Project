package com.igav.igav_project.Model.Entity.Commerce.Rental;

import com.igav.igav_project.Model.Entity.Commerce.Operation.Operation;
import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;

import jakarta.persistence.*;

@Entity 
@Table(name = "rentals")
public class Rental {

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

    


}
