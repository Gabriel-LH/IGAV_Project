package com.igav.igav_project.Model.Entity.Catalog.Attribute.AttributeValue;

import com.igav.igav_project.Model.Entity.Catalog.Attribute.AttributeType.AttributeType;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table (name = "attribute_values")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributeValue {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "tenant_id", nullable = false)
    private Long tenantId;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "attribute_type_id", nullable = false)
    private AttributeType attributeTypeId;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "valor", nullable = false)
    private String valor;

    @Column(name = "hex_color")
    private String hexColor;

    @Column(name = "is_active")
    private boolean isActive;

}
