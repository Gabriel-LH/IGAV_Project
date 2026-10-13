package com.igav.igav_project.Model.Entity.Commerce.Reservation;

import com.igav.igav_project.Model.Entity.Catalog.Product.Product;
import com.igav.igav_project.Model.Entity.Catalog.Product.ProductVariant.ProductVariant;
import com.igav.igav_project.Model.Entity.Inventory.InventoryItem.InventoryItem;
import com.igav.igav_project.Model.Entity.Inventory.SockLot.StockLot;
import com.igav.igav_project.Model.Entity.Pricing.Promotion.Promotion;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;
import com.igav.igav_project.Model.Shared.ValueObjects.Moneda;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table (name = "reservation_items")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class ReservationItem {


    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación principal de dominio (Escritura y Navegación oficial)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    // Columna redundante optimizada solo para lectura (Consultas rápidas / Seguridad / Índices)
    @Column(name = "operation_id", insertable = false, updatable = false)
    private Long operationId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "stocklot_id", nullable = false)
    private StockLot stockLot;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "promotion_id", nullable = false)
    private Promotion promotion;

    @Column (name = "quantity", nullable = false)
    private float quantity;
    @Column (name = "price_at_moment", nullable = false)
    private Moneda priceAtMoment;
    @Column (name = "list_price", nullable = true)
    private Moneda listPrice;
    @Column (name = "total_discount", nullable = false)
    private Moneda totalDiscount;
    @Column (name = "discount_reason")
    private String discountReason;
    @Column (name = "bundle_group_id", nullable = true)
    private String bundleGroupId;
    @Column (name = "notes", nullable = true)
    private String notes;
    @Enumerated (EnumType.STRING)
    @Column (name = "reservation_status", nullable = false)
    private  ReservationStatus reservationStatus;
    @Column (name = "is_serial", nullable = false)
    private boolean isSerial;
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "inventory_item_id")
    private InventoryItem inventoryItem;

    @Embedded 
    private AuditMetadata auditMetadata;
}
