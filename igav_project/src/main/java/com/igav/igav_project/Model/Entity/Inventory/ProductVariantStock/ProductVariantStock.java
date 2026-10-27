package com.igav.igav_project.Model.Entity.Inventory.ProductVariantStock;

import com.igav.igav_project.Model.Entity.Catalog.Product.ProductVariant.ProductVariant;
import com.igav.igav_project.Model.Entity.Inventory.SockLot.StockBalance;
import com.igav.igav_project.Model.Entity.Organization.Branch.Branch;
import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table (name = "product_variant_stocks")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class ProductVariantStock {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column (name = "product_id", nullable = false)
    private Long productId;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant productVariant;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "quantity", column = @Column(name = "stock_quantity", nullable = false)),
        @AttributeOverride(name = "averageCost.monto", column = @Column(name = "average_cost", precision = 12, scale = 2, nullable = false)),
        @AttributeOverride(name = "averageCost.tipoMoneda.codigo", column = @Column(name = "average_cost_currency", length = 3, nullable = false)),
        @AttributeOverride(name = "totalValue.monto", column = @Column(name = "total_value", precision = 12, scale = 2, nullable = false)),
        @AttributeOverride(name = "totalValue.tipoMoneda.codigo", column = @Column(name = "total_value_currency", length = 3, nullable = false))
    })
    private StockBalance stockBalance;

}
