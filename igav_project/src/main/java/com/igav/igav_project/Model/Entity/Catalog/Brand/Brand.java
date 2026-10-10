package com.igav.igav_project.Model.Entity.Catalog.Brand;

import java.util.List;

import com.igav.igav_project.Model.Entity.Catalog.Brand.Model.Model;
import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;

@Entity 
@Table (name = "brands")
public class Brand {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "tenant", nullable = false)
    private Tenant tenant;

    @Column (name = "name", nullable = false, length = 20)
    private String name;

    @Column (name = "slug", nullable = false)
    private String slug;

    @Column (name = "description", nullable = false)
    private String description;

    @Column (name = "image", nullable = false)
    private String image;

    @Embedded 
    private AuditMetadata auditMetadata;

    @OneToMany (mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Model> models;

}
