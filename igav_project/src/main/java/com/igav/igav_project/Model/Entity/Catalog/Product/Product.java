package com.igav.igav_project.Model.Entity.Catalog.Product;

import java.util.ArrayList;
import java.util.List;

import com.igav.igav_project.Model.Entity.Catalog.Brand.Model.Model;
import com.igav.igav_project.Model.Entity.Catalog.Category.Category;

import jakarta.persistence.*;

@Entity 
@Table (name = "products")
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
    private List<String> image = new ArrayList<>();

}
