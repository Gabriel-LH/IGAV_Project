package com.igav.igav_project.Model.Entity.Commerce.Sale;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.igav.igav_project.Model.Entity.Catalog.Product.Product;
import com.igav.igav_project.Model.Entity.Catalog.Product.ProductVariant.ProductVariant;
import com.igav.igav_project.Model.Entity.Inventory.InventoryItem.InventoryItem;
import com.igav.igav_project.Model.Entity.Inventory.SockLot.StockLot;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table (name = "sale_items")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class SaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_lot_id")
    private StockLot stockLot;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id")
    private InventoryItem inventoryItem;

    @Column(name = "price_at_moment", precision = 12, scale = 2, nullable = false)
    private BigDecimal priceAtMoment;

    @Column(name = "list_price", precision = 12, scale = 2, nullable = false)
    private BigDecimal listPrice;

    @Column(name = "quantity", precision = 12, scale = 4, nullable = false)
    private BigDecimal quantity;

    @Column(name = "discount_amount", precision = 12, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "discount_reason")
    private String discountReason;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "variant_code")
    private String variantCode;

    @Column(name = "serial_code")
    private String serialCode;

    @Column(name = "is_serial", nullable = false)
    private boolean isSerial;

    @Column(name = "returned_at")
    private LocalDateTime returnedAt;

    @Column(name = "is_returned", nullable = false)
    private boolean isReturned;

    @Column(name = "is_gift", nullable = false)
    private boolean isGift;

    @Column(name = "is_discount_item", nullable = false)
    private boolean isDiscountItem;

    @Column(name = "return_condition", columnDefinition = "TEXT")
    private String returnCondition;

    @Embedded
    private AuditMetadata auditMetadata;

    @OneToMany (mappedBy = "saleItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SaleCharge> saleCharges = new ArrayList<>();
}
