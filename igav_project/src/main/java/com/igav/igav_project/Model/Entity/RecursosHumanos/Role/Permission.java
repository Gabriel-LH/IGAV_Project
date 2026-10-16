package com.igav.igav_project.Model.Entity.RecursosHumanos.Role;

import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table (name = "permissions")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class Permission {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(name = "key", length = 100, nullable = false)
    private String key;

    @Enumerated(EnumType.STRING)
    @Column(name = "module", nullable = false)
    private ModuleEnum module;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Embedded
    private AuditMetadata auditMetadata;

}
