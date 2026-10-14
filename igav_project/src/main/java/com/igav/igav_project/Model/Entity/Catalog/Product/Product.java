package com.igav.igav_project.Model.Entity.Catalog.Product;

import java.util.ArrayList;
import java.util.List;

import com.igav.igav_project.Model.Entity.Catalog.Brand.Model.Model;
import com.igav.igav_project.Model.Entity.Catalog.Category.Category;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalItem;
import com.igav.igav_project.Model.Entity.Commerce.Reservation.ReservationItem;
import com.igav.igav_project.Model.Entity.Commerce.Sale.SaleItem;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table (name = "products")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class Product {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "tenant_id", insertable = false, updatable = false)
    private Long tenantId;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "base_sku", nullable = false)
    private String baseSku;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "model_id", nullable = false)
    private Model model;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "category_id", nullable = false)
    private Category category;

    @Column(name = "description")
    private String description;

    @Column(name = "is_serial")
    private boolean isSerial;

    @Column(name = "is_rent")
    private boolean isRent;

    @Column(name = "is_sellable")
    private boolean isSellable;

    @Column(name = "deleted_reason")
    private String deletedReason;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    @Builder.Default
    private List<String> image = new ArrayList<>();

    @OneToMany (mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReservationItem> reservationItems = new ArrayList<>();
    @OneToMany (mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RentalItem> rentalItems = new ArrayList<>();
    @OneToMany (mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SaleItem> saleItems = new ArrayList<>();

}
