package com.igav.igav_project.Model.Entity.Commerce.Rental;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.igav.igav_project.Model.Entity.Client.Client;
import com.igav.igav_project.Model.Entity.Commerce.Guarantee.Guarantee;
import com.igav.igav_project.Model.Entity.Commerce.Operation.Operation;
import com.igav.igav_project.Model.Entity.Commerce.Reservation.Reservation;
import com.igav.igav_project.Model.Entity.Organization.Branch.Branch;
import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;
import com.igav.igav_project.Model.Shared.ValueObjects.Moneda;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table(name = "rentals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name="tenant_id", nullable = false)
    private Tenant tenant;

    // Cada alquiler específico pertenece a UNA sola Operación
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "operation_id", nullable = false)
    private Operation operation;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "reservation_id", nullable = false)
    private Reservation reservation; 

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "client_id", nullable = false)
    private Client client; 

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name ="branch_id", nullable = false)
    private Branch branch;

    @Column(name = "out_date", nullable = false)
    private LocalDateTime outDate;

    @Column(name = "expected_return_date", nullable = false)
    private LocalDateTime expectedReturnDate;

    @Column(name = "actual_return_date")
    private LocalDateTime actualReturnDate;

    @Column(name = "cancel_date")
    private LocalDateTime cancelDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RentalStatus status;

    
    // Si Moneda es una clase mapeada como Embeddable o tipo numérico (BigDecimal):
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "monto", column = @Column(name = "sub_total")),
        @AttributeOverride(name = "tipoMoneda.codigo", column = @Column(name = "sub_total_currency", length = 3))
    })
    private Moneda subTotal;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "monto", column = @Column(name = "total_discount")),
        @AttributeOverride(name = "tipoMoneda.codigo", column = @Column(name = "discount_currency", length = 3))
    })
    private Moneda totalDiscount;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Embedded
    private AuditMetadata auditMetadata;

    // Un alquiler tiene MUCHAS garantías
    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Guarantee> guarantees = new ArrayList<>();
    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RentalItem> rentalItems = new ArrayList<>();


}
