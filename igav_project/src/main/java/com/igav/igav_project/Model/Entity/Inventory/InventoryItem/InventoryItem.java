package com.igav.igav_project.Model.Entity.Inventory.InventoryItem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.igav.igav_project.Model.Entity.Catalog.Product.ProductVariant.ProductVariant;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalItem;
import com.igav.igav_project.Model.Entity.Commerce.Sale.SaleItem;
import com.igav.igav_project.Model.Entity.Organization.Branch.Branch;
import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table (name = "inventory_items")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class InventoryItem {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column (name = "serial_code", nullable = false)
    private String serialCode;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @Column (name = "product_id", insertable = false, updatable = false)
    private Long productId;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "branch_id", nullable = false)
    private Branch branch;

    @Column (name = "is_for_rent", nullable = false)
    private boolean isForRent;
    @Column (name = "is_for_sale", nullable = false )
    private boolean isForSale;
    @Column (name = "usageCount", nullable = false)
    private int usageCount;
    @Column (name = "last_maintance", nullable = false)
    private LocalDate lastMaintance;
    
    @Enumerated (EnumType.STRING)
    @Column (name = "inventory_condition", nullable = false)
    private InventoryCondition inventoryCondition;
    
    @Enumerated (EnumType.STRING)
    @Column (name = "inventory_status", nullable = false)
    private InventoryStatus inventoryStatus;

    @OneToMany (mappedBy = "inventory_item", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RentalItem> rentalItems = new ArrayList<>();
    @OneToMany (mappedBy = "inventory_item", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SaleItem> saleItems = new ArrayList<>();
}   
