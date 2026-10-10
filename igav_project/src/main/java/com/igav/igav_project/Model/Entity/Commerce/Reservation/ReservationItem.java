package com.igav.igav_project.Model.Entity.Commerce.Reservation;

import com.igav.igav_project.Model.Entity.Catalog.Product.Product;

import jakarta.persistence.*;

@Entity 
@Table (name = "reservation_items")
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

}
