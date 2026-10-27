package com.igav.igav_project.Model.Entity.Inventory.SockLot.StockMovement;

import com.igav.igav_project.Model.Entity.Commerce.Operation.Operation;
import com.igav.igav_project.Model.Entity.Inventory.SockLot.StockLot;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;
import com.igav.igav_project.Model.Shared.ValueObjects.Moneda;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table (name = "stock_movements")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class StockMovement {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_lot_id", nullable = false)
    private StockLot stockLot;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_id")
    private Operation operation;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private StockMovementType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "direction", nullable = false)
    private MovementDirection direction;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "changed_by", nullable = false)
    private String changedBy;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "monto", column = @Column(name = "unit_cost_monto", precision = 12, scale = 4, nullable = false)),
        @AttributeOverride(name = "tipoMoneda.codigo", column = @Column(name = "unit_cost_moneda", length = 3, nullable = false))
    })
    private Moneda unitCost;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "monto", column = @Column(name = "total_cost_monto", precision = 12, scale = 2, nullable = false)),
        @AttributeOverride(name = "tipoMoneda.codigo", column = @Column(name = "total_cost_moneda", length = 3, nullable = false))
    })
    private Moneda totalCost;

    @Column(name = "running_balance", nullable = false)
    private int runningBalance;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "monto", column = @Column(name = "running_unit_cost_monto", precision = 12, scale = 4, nullable = false)),
        @AttributeOverride(name = "tipoMoneda.codigo", column = @Column(name = "running_unit_cost_moneda", length = 3, nullable = false))
    })
    private Moneda runningUnitCost;

    @Embedded
    private AuditMetadata auditMetadata;

}
