package com.igav.igav_project.Model.Entity.Catalog.Category;

import java.util.List;

import com.igav.igav_project.Model.Entity.Catalog.Product.Product;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;

@Entity 
@Table (name = "categories")
public class Category {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;
    @Column(name = "description")
    private String description;
    @Column(name = "parent_category_id")
    private String parentCategoryId;
    @Column(name = "level")
    private int level;
    @Column(name = "path")
    private String path;
    @Column(name = "image")
    private String image;
    @Column(name = "color")
    private String color;
    @Column(name = "icon")
    private String icon;
    @Column(name = "slug")
    private String slug;
    @Column(name = "order_index") // "order" es palabra reservada en SQL
    private int order;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "show_in_pos")
    private boolean showInPos;
    @Column(name = "show_in_ecommerce")
    private boolean showInEcommerce;
    @Column(name = "product_count")
    private int productCount;
    @Column(name = "total_product_count")
    private int totalProductCount;

    @Embedded 
    private AuditMetadata auditMetadata;

    @OneToMany (mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;
}
