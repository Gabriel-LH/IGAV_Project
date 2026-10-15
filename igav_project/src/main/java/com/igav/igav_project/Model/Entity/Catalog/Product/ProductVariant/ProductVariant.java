package com.igav.igav_project.Model.Entity.Catalog.Product.ProductVariant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.igav.igav_project.Model.Entity.Catalog.Product.Product;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalItem;
import com.igav.igav_project.Model.Entity.Commerce.Reservation.ReservationItem;
import com.igav.igav_project.Model.Entity.Commerce.Sale.SaleItem;
import com.igav.igav_project.Model.Entity.Inventory.InventoryItem.InventoryItem;
import com.igav.igav_project.Model.Entity.Inventory.Movement.InventoryMovement;
import com.igav.igav_project.Model.Entity.Inventory.ProductVariantStock.ProductVariantStock;
import com.igav.igav_project.Model.Entity.Inventory.StockAdjustment.StockAdjustmentItem;
import com.igav.igav_project.Model.Entity.Inventory.Transfer.TransferItem;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table (name = "product_variants")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class ProductVariant {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "product_id", nullable = false)
    private Product product;

    @Column(name = "variant_code", nullable = false)
    private String variantCode;

    @Column(name = "barcode")
    private String barcode;

    @ElementCollection
    @CollectionTable(name = "product_variant_attributes", joinColumns = @JoinColumn(name = "variant_id"))
    @MapKeyColumn(name = "attribute_key")
    @Column(name = "attribute_value")
    @Builder.Default
    private Map<String, String> attributes = new HashMap<>();

    @Column(name = "price_sell", precision = 12, scale = 2)
    private BigDecimal priceSell;

    @Column(name = "price_rent", precision = 12, scale = 2)
    private BigDecimal priceRent;

    @Column(name = "rent_unit")
    private String rentUnit;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "purchase_price", precision = 12, scale = 2)
    private BigDecimal purchasePrice;

    @Embedded
    private AuditMetadata auditMetadata;

    @ElementCollection
    @CollectionTable(name = "product_variant_images", joinColumns = @JoinColumn(name = "variant_id"))
    @Column(name = "image_url")
    @Builder.Default
    private List<String> imageUrl = new ArrayList<>();

    @Column(name = "variant_signature")
    private String variantSignature;

    @OneToMany (mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReservationItem> reservationItems = new ArrayList<>();

    @OneToMany (mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InventoryItem> inventoryItems = new ArrayList<>();

    @OneToMany (mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RentalItem> renatlItems = new ArrayList<>();
    @OneToMany (mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SaleItem> saleItems = new ArrayList<>();
    @OneToMany (mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InventoryMovement> inventoryMovements = new ArrayList<>();
    @OneToMany (mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductVariantStock> productVariantStocks  = new ArrayList<>();
    @OneToMany (mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TransferItem> transferItems  = new ArrayList<>();
     @OneToMany (mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<StockAdjustmentItem> stockAdjustmentItems  = new ArrayList<>();

}
