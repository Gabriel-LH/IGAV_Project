package com.igav.igav_project.Model.Entity.Commerce.Operation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.igav.igav_project.Model.Entity.Client.Client;
import com.igav.igav_project.Model.Entity.Commerce.Guarantee.Guarantee;
import com.igav.igav_project.Model.Entity.Commerce.Rental.Rental;
import com.igav.igav_project.Model.Entity.Commerce.Reservation.Reservation;
import com.igav.igav_project.Model.Entity.Organization.Branch.Branch;
import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;
import com.igav.igav_project.Model.Entity.User.User;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;
import com.igav.igav_project.Model.Shared.ValueObjects.Moneda;

import jakarta.persistence.*;

@Entity 
@Table (name = "operations")
public class Operation {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn (name = "tenant_id", nullable = false)
    @ManyToOne (fetch = FetchType.LAZY)
    private Tenant tenant;

    @JoinColumn (name ="branch_id", nullable = false)
    @ManyToOne (fetch = FetchType.LAZY)
    private Branch branch;

    @JoinColumn (name = "seller_id", nullable = false)
    @ManyToOne (fetch = FetchType.LAZY)
    private User user;

    @Enumerated (EnumType.STRING)
    @Column (name = "operation_type", nullable = false)
    private OperationType operationType;

    @Enumerated (EnumType.STRING)
    @Column (name = "customer_mode", nullable = false)
    private CustomerMode customerMode;

    @JoinColumn (name = "client_id", nullable = false)
    @ManyToOne  (fetch = FetchType.LAZY)
    private Client client;

    @Enumerated (EnumType.STRING)
    @Column (name = "operation_status", nullable = false)
    private  OperationStatus operationStatus;

    @Enumerated (EnumType.STRING)
    @Column (name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Embedded 
    @Column (name = "subtotal", nullable = true)
    private Moneda subTotal;
    @Embedded 
    @Column (name = "discount_amount", nullable = true)
    private Moneda discountAmount;
    @Embedded 
    @Column (name = "total_amount", nullable = false)
    private Moneda totalAmount;
    @Embedded 
    @Column (name = "rounding_amount", nullable = true)
    private Moneda roundingAmount;
    @Embedded 
    @Column (name = "tax_amount", nullable = true)
    private Moneda taxAmount;
    @Embedded 
    @Column (name = "total_before_rounding", nullable = true)
    private Moneda totalBeforeRounding;
    @Column (name = "tax_rate", nullable = true)
    private double taxRate;

    @Column (name = "ocurred_at")
    private Date ocurredAt;

    @Column (name = "config_snapshot", nullable = false)
    private String configSnapshot;
    @Column (name = "config_version", nullable = true)
    private int configVersion;
    @Column (name = "policy_snapshot", nullable = false)
    private String policySnapshot;
    @Column (name = "policy_version", nullable = true)
    private int policyVersion;

    @Embedded 
    @Column (name = "audit_metadata", nullable = false)
    private AuditMetadata auditMetadata;

    // Una Operación contiene MUCHOS ítems alquilados
    @OneToMany(mappedBy = "operation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rental> rentals = new ArrayList<>();
    @OneToMany(mappedBy = "operation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();
    @OneToMany (mappedBy = "operation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Guarantee> guarantees;




    public Operation() {
    }




}
