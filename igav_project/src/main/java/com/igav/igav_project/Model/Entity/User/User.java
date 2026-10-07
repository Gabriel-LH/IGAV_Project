package com.igav.igav_project.Model.Entity.User;

import com.igav.igav_project.Model.Entity.Organization.Branch.Branch;
import com.igav.igav_project.Model.Shared.ValueObjects.Address;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;
import com.igav.igav_project.Model.Shared.ValueObjects.DocumentoIdentidad;
import com.igav.igav_project.Model.Shared.ValueObjects.Email;
import com.igav.igav_project.Model.Shared.ValueObjects.Telefono;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Entidad JPA que representa a un Usuario dentro del sistema I.G.A.V.
 * Puede pertenecer a una tienda específica o ser un Administrador Global.
 * Cumple con los requerimientos RF-01 y RF-02.
 *
 * @author IGAV Development Team
 */
@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Embedded
    @AttributeOverride(name = "email", column = @Column(name = "email", nullable = false, unique = true))
    private Email email;

    @Embedded
    @AttributeOverride(name = "numero", column = @Column(name = "documento_numero", nullable = false))
    @AttributeOverride(name = "tipo", column = @Column(name = "documento_tipo", nullable = false))
    private DocumentoIdentidad documentoIdentidad; 

    @Column(name = "image")
    private String image;
    
    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "telefono", nullable = true))
    private Telefono telefono; 

    @Column(name = "telefono_verified", nullable = false)
    private boolean telefonoVerified;

    @Embedded
    private Address address;

    @ManyToMany
    @JoinTable(
        name = "user_branch", // Nombre de la tabla intermedia
        joinColumns = @JoinColumn(name = "user_id"), // FK hacia la tabla User
        inverseJoinColumns = @JoinColumn(name = "branch_id") // FK hacia la tabla Branch
    )
    private Set<Branch> branches = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status_user", nullable = false)
    private StatusUser statusUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "global_role", nullable = false)
    private GlobalRole globalRole;

    @Embedded
    private AuditMetadata auditMetadata;

    /**
     * Constructor vacío requerido obligatoriamente por JPA / Hibernate.
     */
    protected User() {
    }

    /**
     * Constructor privado para forzar la instanciación a través del método de fábrica estático.
     */
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

    /**
     * Método de fábrica estático para la creación limpia de usuarios.
     *
     * @param nombres Nombres del usuario.
     * @param apellidos Apellidos del usuario.
     * @param email Correo electrónico principal.
     * @param documentoIdentidad Documento de identidad oficial.
     * @param image URL o ruta de la foto de perfil.
     * @param telefono Teléfono de contacto.
     * @param globalRole Rol asignado (SUPER_ADMIN, ADMIN_TIENDA, VENDEDOR, ENCARGADO_ALMACEN_TINTORERIA).
     * @param address Dirección del usuario.
     * @param store Tienda a la que pertenece el usuario (null si es SUPER_ADMIN).
     * @param createdBy Usuario o sistema creador.
     * @return Nueva instancia de {@link User}.
     */
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

    /**
     * Marca el correo electrónico del usuario como verificado.
     *
     * @param updatedBy Usuario responsable de la actualización.
     */
    public void verifyEmail(String updatedBy) {
        this.emailVerified = true;
        this.auditMetadata = this.auditMetadata.update(updatedBy);
    }

    /**
     * Activa el estado del usuario.
     *
     * @param updatedBy Usuario responsable de la actualización.
     */
    public void markAsActive(String updatedBy) {
        this.statusUser = StatusUser.ACTIVO;
        this.auditMetadata = this.auditMetadata.update(updatedBy);
    }

    /**
     * Inactiva el estado del usuario.
     *
     * @param updatedBy Usuario responsable de la actualización.
     */
    public void markAsInactive(String updatedBy) {
        this.statusUser = StatusUser.INACTIVO;
        this.auditMetadata = this.auditMetadata.update(updatedBy);
    }


    public void setBranches(Set<Branch> branches) {
        this.branches = branches;
    }

    // Getters para exponer el estado de forma segura
    public Set<Branch> getBranches() { return branches; }
    public Long getId() { return id; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public Email getEmail() { return email; }
    public DocumentoIdentidad getDocumentoIdentidad() { return documentoIdentidad; }
    public String getImage() { return image; }
    public boolean isEmailVerified() { return emailVerified; }
    public Telefono getTelefono() { return telefono; }
    public boolean isTelefonoVerified() { return telefonoVerified; }
    public Address getAddress() { return address; }
    public StatusUser getStatusUser() { return statusUser; }
    public GlobalRole getGlobalRole() { return globalRole; }
    public AuditMetadata getAuditMetadata() { return auditMetadata; }
}