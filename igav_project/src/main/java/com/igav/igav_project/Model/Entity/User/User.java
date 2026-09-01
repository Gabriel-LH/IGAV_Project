package com.igav.igav_project.Model.Entity.User;

import com.igav.igav_project.Model.Shared.ValueObjects.Address;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;
import com.igav.igav_project.Model.Shared.ValueObjects.DocumentoIdentidad;
import com.igav.igav_project.Model.Shared.ValueObjects.Email;
import com.igav.igav_project.Model.Shared.ValueObjects.Telefono;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombres;
    private String apellidos;

    @Embedded
    @AttributeOverride(name = "email", column = @Column(name = "email", nullable = false, unique = true))
    private Email email;

    @Embedded
    @Column(name = "documento_identidad", nullable = false)
    private DocumentoIdentidad documentoIdentidad; 
    private String image;
    
    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    @Embedded
    private Telefono telefono; 

    @Column(name = "telefono_verified", nullable = false)
    private boolean telefonoVerified;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_user", nullable = false)
    private StatusUser statusUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "global_role", nullable = false)
    private GlobalRole globalRole;

    @Embedded
    @Column(name = "audit_metadata", nullable = false)
    private AuditMetadata auditMetadata;

    // Constructor vacío protegido requerido obligatoriamente por Hibernate (JPA)
    protected User() {
    }

    // Constructor privado para forzar el uso del Factory Method
    private User(
            String nombres,
            String apellidos,
            Email email,
            DocumentoIdentidad documentoIdentidad,
            String image,
            Telefono telefono,
            GlobalRole globalRole,
            Address address,
            String createdBy
    ) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.documentoIdentidad = documentoIdentidad;
        this.image = image;
        this.telefono = telefono;
        this.globalRole = globalRole;
        this.address = address;
        this.emailVerified = false;
        this.telefonoVerified = false;
        this.statusUser = StatusUser.ACTIVO;
        this.auditMetadata = AuditMetadata.create(createdBy);
    }

    // Factory Method limpio para la creación de usuarios
    public static User create(
            String nombres,
            String apellidos,
            Email email,
            DocumentoIdentidad documentoIdentidad,
            String image,
            Telefono telefono,
            GlobalRole globalRole,
            Address address,
            String createdBy
    ) {
        return new User(nombres, apellidos, email, documentoIdentidad, image, telefono, globalRole, address, createdBy);
    }

    // Métodos de negocio 
    public void verifyEmail(String updatedBy) {
        this.emailVerified = true;
        this.auditMetadata = this.auditMetadata.update(updatedBy);
    }

    public void markAsActive(String updatedBy) {
        this.statusUser = StatusUser.ACTIVO;
        this.auditMetadata = this.auditMetadata.update(updatedBy);
    }

    public void markAsInactive(String updatedBy) {
        this.statusUser = StatusUser.INACTIVO;
        this.auditMetadata = this.auditMetadata.update(updatedBy);
    }

    // Getters para exponer el estado de forma segura (Inmutabilidad externa)
    public Long getId() { return id; }
    public String getNombres() { return nombres; }
    public String getContent() { return apellidos; }
    public Email getEmail() { return email; }
    public DocumentoIdentidad getDni() { return documentoIdentidad; }
    public String getImage() { return image; }
    public boolean isEmailVerified() { return emailVerified; }
    public Telefono getTelefono() { return telefono; }
    public boolean isTelefonoVerified() { return telefonoVerified; }
    public Address getAddress() { return address; }
    public StatusUser getStatusUser() { return statusUser; }
    public GlobalRole getGlobalRole() { return globalRole; }
    public AuditMetadata getAuditMetadata() { return auditMetadata; }
}