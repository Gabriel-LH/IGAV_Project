package com.igav.igav_project.Model.Entity.Order;

import com.igav.igav_project.Model.Entity.Store.Store;
import com.igav.igav_project.Model.Shared.ValueObjects.Address;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;
import com.igav.igav_project.Model.Shared.ValueObjects.DocumentoIdentidad;
import com.igav.igav_project.Model.Shared.ValueObjects.Email;
import com.igav.igav_project.Model.Shared.ValueObjects.Telefono;

import jakarta.persistence.*;

/**
 * Entidad JPA que representa a un Cliente final que realiza alquileres o compras en la tienda.
 * Cumple con los requerimientos RF-06 y RF-07.
 *
 * @author IGAV Development Team
 */
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Embedded
    @AttributeOverride(name = "numero", column = @Column(name = "documento_numero", nullable = false))
    @AttributeOverride(name = "tipo", column = @Column(name = "documento_tipo", nullable = false))
    private DocumentoIdentidad documentoIdentidad;

    @Embedded
    @AttributeOverride(name = "email", column = @Column(name = "email_cliente"))
    private Email email;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "telefono_cliente", nullable = false))
    private Telefono telefono;

    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Embedded
    private AuditMetadata auditMetadata;

    /**
     * Constructor protegido para JPA.
     */
    protected Customer() {
    }

    /**
     * Constructor privado de inicialización.
     */
    private Customer(
            String nombres,
            String apellidos,
            DocumentoIdentidad documentoIdentidad,
            Email email,
            Telefono telefono,
            Address address,
            Store store,
            String createdBy
    ) {
        if (nombres == null || nombres.isBlank()) {
            throw new IllegalArgumentException("Los nombres del cliente son obligatorios.");
        }
        if (apellidos == null || apellidos.isBlank()) {
            throw new IllegalArgumentException("Los apellidos del cliente son obligatorios.");
        }
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.documentoIdentidad = documentoIdentidad;
        this.email = email;
        this.telefono = telefono;
        this.address = address;
        this.store = store;
        this.auditMetadata = AuditMetadata.create(createdBy);
    }

    /**
     * Método de fábrica estático para instanciar clientes.
     *
     * @param nombres Nombres.
     * @param apellidos Apellidos.
     * @param documentoIdentidad DNI, RUC, etc.
     * @param email Correo electrónico opcional.
     * @param telefono Teléfono de contacto obligatorio.
     * @param address Dirección opcional.
     * @param store Tienda en la que se registra.
     * @param createdBy Usuario creador.
     * @return Nueva instancia de {@link Customer}.
     */
    public static Customer create(
            String nombres,
            String apellidos,
            DocumentoIdentidad documentoIdentidad,
            Email email,
            Telefono telefono,
            Address address,
            Store store,
            String createdBy
    ) {
        return new Customer(nombres, apellidos, documentoIdentidad, email, telefono, address, store, createdBy);
    }

    // Getters
    public Long getId() { return id; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public DocumentoIdentidad getDocumentoIdentidad() { return documentoIdentidad; }
    public Email getEmail() { return email; }
    public Telefono getTelefono() { return telefono; }
    public Address getAddress() { return address; }
    public Store getStore() { return store; }
    public AuditMetadata getAuditMetadata() { return auditMetadata; }
}
