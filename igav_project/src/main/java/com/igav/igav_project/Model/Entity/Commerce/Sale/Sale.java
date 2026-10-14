package com.igav.igav_project.Model.Entity.Commerce.Sale;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.igav.igav_project.Model.Entity.Client.Client;
import com.igav.igav_project.Model.Entity.Commerce.Operation.CustomerMode;
import com.igav.igav_project.Model.Entity.Commerce.Operation.Operation;
import com.igav.igav_project.Model.Entity.Commerce.Reservation.Reservation;
import com.igav.igav_project.Model.Entity.Organization.Branch.Branch;
import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;
import com.igav.igav_project.Model.Entity.User.User;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table (name = "sales")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class Sale {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_id", nullable = false)
    private Operation operation;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_mode", nullable = false)
    private CustomerMode customerMode;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Client client;

    @ManyToOne (fetch = FetchType.LAZY )
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(name = "total_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "sub_total_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal subTotalAmount;

    @Column(name = "total_discount", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalDiscount;

    @Column(name = "sale_date", nullable = false)
    private LocalDateTime saleDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Enumerated (EnumType.STRING)
    @Column (name = "status", nullable = false)
    private SaleStatus status;

    @Column(name = "out_date")
    private LocalDateTime outDate;

    @Column(name = "real_out_date_time")
    private LocalDateTime realOutDateTime;

    @Column(name = "canceled_date")
    private LocalDateTime canceledDate;

    @Column(name = "return_date_time")
    private LocalDateTime returnDateTime;

    @Column(name = "amount_refunded", precision = 12, scale = 2, nullable = false)
    private BigDecimal amountRefunded;

    @Embedded
    private AuditMetadata auditMetadata;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true) 
    @Builder.Default
    private List<SaleItem> saleItems = new ArrayList<>();

}
