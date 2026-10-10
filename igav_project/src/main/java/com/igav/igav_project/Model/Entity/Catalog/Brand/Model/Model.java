package com.igav.igav_project.Model.Entity.Catalog.Brand.Model;


import java.util.List;

import com.igav.igav_project.Model.Entity.Catalog.Brand.Brand;
import com.igav.igav_project.Model.Entity.Catalog.Product.Product;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;

@Entity 
@Table (name = "models")
public class Model {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column  (name = "tenant_id", insertable = false, updatable = false)
    private Long tenantId;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "brand_id", nullable = false)
    private Brand brand;

        @Column (name = "name", nullable = false, length = 20)
    private String name;

    @Column (name = "slug", nullable = false)
    private String slug;

    @Column (name = "description", nullable = false)
    private String description;

    @Column (name = "year", nullable = false )
    private int Year;

    @Column (name = "is_active", nullable = false)
    private boolean isActive;

    @Embedded 
    private AuditMetadata auditMetadata;

    @OneToMany (mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;
}
