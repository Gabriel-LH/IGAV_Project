package com.igav.igav_project.Model.Entity.Catalog.Attribute.AttributeType;

import java.util.ArrayList;
import java.util.List;

import com.igav.igav_project.Model.Entity.Catalog.Attribute.AttributeValue.AttributeValue;
import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;

import jakarta.persistence.*;
import lombok.*;
@Entity 
@Table (name = "attribute_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributeType {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "tenant_id")
    private Tenant tenant;

    @Column (name = "code", nullable = false)
    private String code;

    @Enumerated (EnumType.STRING)
    @Column (name = "attribute_input_type")
    private AttributeInputType attributeInputType;

    @Column (name = "is_variante", nullable = true)
    private boolean isVariant;

    @Column (name = "affect_sku", nullable = true)
    private boolean affectSku;

    @Column (name = "is_active", nullable = true)
    private boolean isActive;

    @OneToMany(mappedBy = "attributeType", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AttributeValue> values = new ArrayList<>();

}
