package com.igav.igav_project.Model.Entity.Catalog.Product.ProductVariant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.igav.igav_project.Model.Entity.Catalog.Product.Product;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;

@Entity 
@Table (name = "product_variants")
public class ProductVariant {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "product_id", nullable = false)
    private Product product;

    @Column(name = "variant_code", nullable = false)
    private String variantCode;

    @Column(name = "barcode")
    private String barcode;

    @ElementCollection
    @CollectionTable(name = "product_variant_attributes", joinColumns = @JoinColumn(name = "variant_id"))
    @MapKeyColumn(name = "attribute_key")
    @Column(name = "attribute_value")
    private Map<String, String> attributes = new HashMap<>();

    @Column(name = "price_sell", precision = 12, scale = 2)
    private BigDecimal priceSell;

    @Column(name = "price_rent", precision = 12, scale = 2)
    private BigDecimal priceRent;

    @Column(name = "rent_unit")
    private String rentUnit;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "purchase_price", precision = 12, scale = 2)
    private BigDecimal purchasePrice;

    @Embedded
    private AuditMetadata auditMetadata;

    @ElementCollection
    @CollectionTable(name = "product_variant_images", joinColumns = @JoinColumn(name = "variant_id"))
    @Column(name = "image_url")
    private List<String> imageUrl = new ArrayList<>();

    @Column(name = "variant_signature")
    private String variantSignature;
}
