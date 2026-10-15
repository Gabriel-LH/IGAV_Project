package com.igav.igav_project.Model.Entity.Inventory.StockAdjustment;

import com.igav.igav_project.Model.Entity.Catalog.Product.ProductVariant.ProductVariant;
import com.igav.igav_project.Model.Entity.Inventory.SockLot.StockLot;

import jakarta.persistence.*;
import lombok.*;


@Entity 
@Table (name = "stock_adjustment_items")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class StockAdjustmentItem {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_lot_id") // Opcional si se manejan lotes
    private StockLot stockLot;

    @Column(name = "system_quantity", nullable = false)
    private int systemQuantity; // Lo que dice el sistema

    @Column(name = "actual_quantity", nullable = false)
    private int actualQuantity; // Lo que se contó físicamente

    @Column(name = "difference", nullable = false)
    private int difference; // Variación (actualQuantity - systemQuantity)

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

}
