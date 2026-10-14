package com.igav.igav_project.Model.Entity.Inventory.SockLot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalItem;
import com.igav.igav_project.Model.Entity.Commerce.Reservation.ReservationItem;
import com.igav.igav_project.Model.Entity.Commerce.Sale.SaleItem;
import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table (name = "stock_lots")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class StockLot {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "product_variant_id")
    private String productVariantId;

    @Column(name = "warehouse_id", nullable = false)
    private String warehouseId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "lote_number")
    private String loteNumber;

    @Column(name = "is_for_rent")
    private boolean isForRent;

    @Column(name = "is_for_sale")
    private boolean isForSale;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StockLotStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition", nullable = false)
    private StockLotCondition condition;

    @Embedded
    private AuditMetadata auditMetadata;

    @OneToMany (mappedBy = "stockLot", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReservationItem> reservationItems = new  ArrayList<>();
    @OneToMany (mappedBy = "stockLot", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RentalItem> rentalItems = new ArrayList<>();
    @OneToMany (mappedBy = "stockLot", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SaleItem> saleItems = new ArrayList<>();

}
