package com.igav.igav_project.Model.Entity.Commerce.Rental;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.igav.igav_project.Model.Entity.Catalog.Product.Product;
import com.igav.igav_project.Model.Entity.Catalog.Product.ProductVariant.ProductVariant;
import com.igav.igav_project.Model.Entity.Commerce.Operation.Operation;
import com.igav.igav_project.Model.Entity.Inventory.InventoryItem.InventoryItem;
import com.igav.igav_project.Model.Entity.Inventory.SockLot.StockLot;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table (name = "rental_items")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class RentalItem {

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_id", nullable = false)
    private Operation operation;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne  (fetch = FetchType.LAZY)
    @Column(name = "variant_id", nullable = false)
    private ProductVariant productVariant;
    
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_lot_id")
    private StockLot stockLot;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id")
    private InventoryItem inventoryItem;

    @Column(name = "price_at_moment", precision = 12, scale = 2, nullable = false)
    private BigDecimal priceAtMoment;

    @Column(name = "quantity", precision = 12, scale = 4, nullable = false)
    private BigDecimal quantity;

    @Column(name = "condition_out", nullable = false)
    private String conditionOut;

    @Column(name = "condition_in")
    private String conditionIn;

    @Column(name = "is_damage", nullable = false)
    private boolean isDamage;

    @Column(name = "damage_notes", columnDefinition = "TEXT")
    private String damageNotes;

    @Column(name = "discount_amount", precision = 12, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "discount_reason")
    private String discountReason;

    @Column(name = "promotion_snapshot", columnDefinition = "TEXT")
    private String promotionSnapshot; // JSON o texto de la regla aplicada

    @Column(name = "product_name")
    private String productName;

    @Column(name = "variant_code")
    private String variantCode;

    @Column(name = "serial_code")
    private String serialCode;

    @Column(name = "is_serial", nullable = false)
    private boolean isSerial;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "list_price", precision = 12, scale = 2, nullable = false)
    private BigDecimal listPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RentalItemStatus status;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Embedded
    private AuditMetadata auditMetadata;

    @OneToMany (mappedBy = "rental_item", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RentalCharge> rentalCharges = new ArrayList<>();
}
