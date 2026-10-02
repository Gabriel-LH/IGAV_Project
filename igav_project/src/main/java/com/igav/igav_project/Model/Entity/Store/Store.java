package com.igav.igav_project.Model.Entity.Store;

import com.igav.igav_project.Model.Shared.ValueObjects.Address;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;
import com.igav.igav_project.Model.Shared.ValueObjects.DocumentoIdentidad;
import com.igav.igav_project.Model.Shared.ValueObjects.Email;
import com.igav.igav_project.Model.Shared.ValueObjects.Telefono;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Entidad JPA que representa una Tienda / Negocio dentro de la plataforma SaaS multi-tienda.
 * Cumple con el requerimiento RF-01 (Registro y administración de tiendas suscriptoras).
 *
 * @author IGAV Development Team
 */
@Entity
@Table(name = "stores")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_comercial", nullable = false, length = 150)
    private String nombreComercial;

    @Column(name = "razon_social", nullable = false, length = 150)
    private String razonSocial;

    @Embedded
    @AttributeOverride(name = "numero", column = @Column(name = "ruc_numero", nullable = false, unique = true))
    @AttributeOverride(name = "tipo", column = @Column(name = "ruc_tipo", nullable = false))
    private DocumentoIdentidad ruc;

    @Embedded
    @AttributeOverride(name = "email", column = @Column(name = "email_tienda", nullable = false))
    private Email email;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "telefono_tienda", nullable = false))
    private Telefono telefono;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StoreStatus status;

    @Embedded
    private AuditMetadata auditMetadata;

    /**
     * Constructor protegido requerido por JPA/Hibernate.
     */
    protected Store() {
    }

    /**
     * Constructor privado de inicialización.
     */
    private Store(
            String nombreComercial,
            String razonSocial,
            DocumentoIdentidad ruc,
            Email email,
            Telefono telefono,
            Address address,
            String createdBy
    ) {
        if (nombreComercial == null || nombreComercial.isBlank()) {
            throw new IllegalArgumentException("El nombre comercial es obligatorio.");
        }
        if (razonSocial == null || razonSocial.isBlank()) {
            throw new IllegalArgumentException("La razón social es obligatoria.");
        }
        this.nombreComercial = nombreComercial;
        this.razonSocial = razonSocial;
        this.ruc = ruc;
        this.email = email;
        this.telefono = telefono;
        this.address = address;
        this.status = StoreStatus.ACTIVA;
        this.auditMetadata = AuditMetadata.create(createdBy);
    }

    /**
     * Método de fábrica estático para la creación de una nueva tienda.
     *
     * @param nombreComercial Nombre comercial de la tienda.
     * @param razonSocial Razón social de la empresa.
     * @param ruc Documento de identidad tipo RUC.
     * @param email Correo electrónico de contacto.
     * @param telefono Teléfono de contacto de la tienda.
     * @param address Dirección física de la tienda.
     * @param createdBy Usuario o sistema que registra la tienda.
     * @return Nueva instancia de {@link Store}.
     */
    public static Store create(
            String nombreComercial,
            String razonSocial,
            DocumentoIdentidad ruc,
            Email email,
            Telefono telefono,
            Address address,
            String createdBy
    ) {
        return new Store(nombreComercial, razonSocial, ruc, email, telefono, address, createdBy);
    }

    /**
     * Marca la tienda como suspendida.
     *
     * @param updatedBy Usuario responsable de la acción.
     */
    public void suspend(String updatedBy) {
        this.status = StoreStatus.SUSPENDIDA;
        this.auditMetadata = this.auditMetadata.update(updatedBy);
    }

    /**
     * Reactiva una tienda suspendida.
     *
     * @param updatedBy Usuario responsable de la acción.
     */
    public void activate(String updatedBy) {
        this.status = StoreStatus.ACTIVA;
        this.auditMetadata = this.auditMetadata.update(updatedBy);
    }

    // Getters
    public Long getId() { return id; }
    public String getNombreComercial() { return nombreComercial; }
    public String getRazonSocial() { return razonSocial; }
    public DocumentoIdentidad getRuc() { return ruc; }
    public Email getEmail() { return email; }
    public Telefono getTelefono() { return telefono; }
    public Address getAddress() { return address; }
    public StoreStatus getStatus() { return status; }
    public AuditMetadata getAuditMetadata() { return auditMetadata; }
}
