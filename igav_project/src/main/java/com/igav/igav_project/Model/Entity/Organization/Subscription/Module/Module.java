package com.igav.igav_project.Model.Entity.Organization.Subscription.Module;

import jakarta.persistence.*;
import com.igav.igav_project.Model.Shared.ValueObjects.*;
import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;

@Entity 
@Table(name = "modules")
public class Module {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "tenant_id", nullable = false)
    private Tenant Tenant;
    @Column (name = "nombre", nullable = false, length = 100)
    private String nombre;
    @Column (name = "slug", nullable = false, length = 100)
    private String slug;
    @Column (name = "descripcion", nullable = true, length = 255)
    private String descripcion;
    @Column (name = "is_active", nullable = false)
    private boolean isActive;
    @Column (name = "audit_metadata", nullable = true, columnDefinition = "TEXT")
    @Embedded 
    private AuditMetadata AuditMetadata;

    public Module(Long id, String nombre, String slug, String descripcion, boolean isActive,
            com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata auditMetadata) {
        this.id = id;
        this.nombre = nombre;
        this.slug = slug;
        this.descripcion = descripcion;
        this.isActive = isActive;
        AuditMetadata = auditMetadata;
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getSlug() {
        return slug;
    }
    public void setSlug(String slug) {
        this.slug = slug;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    public AuditMetadata getAuditMetadata() {
        return AuditMetadata;
    }
    public void setAuditMetadata(AuditMetadata auditMetadata) {
        AuditMetadata = auditMetadata;
    }

}
