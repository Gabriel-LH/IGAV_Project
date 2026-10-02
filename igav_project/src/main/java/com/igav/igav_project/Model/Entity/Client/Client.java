package com.igav.igav_project.Model.Entity.Client;

import java.util.Date;

import com.igav.igav_project.Model.Shared.ValueObjects.*;

import jakarta.persistence.*;

@Entity 
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "tenant_id", nullable = false, length = 50)
    public String TenantId;
    
    @Column (name = "document_id", nullable = false, length = 50)
    public DocumentoIdentidad DocumentoIdentidad;
    @Column (name = "name", nullable = false, length = 100) 
    public NombrePersona NombrePersona;
    @Column (name = "email", nullable = false, length = 100)
    public String Email;
    @Column (name = "phone", nullable = true, length = 20)
    public String Telefono;
    @Enumerated (EnumType.STRING)
    @Column (name = "status", nullable = false)
    public ClientStatus ClientStatus;
    @Column (name = "gender", nullable = true, length = 20)
    public String Genero;
    @Column (name = "birth_date", nullable = true)
    public Date FechaNacimiento;
    @Embedded
    public Address Address; // O dividir en strings
    @Column (name = "internal_notes", nullable = true, length = 255)
    public String InternalNotes;
    @Column (name = "metadata", nullable = true, columnDefinition = "TEXT")
    public String Metadata = "{}"; //Posible metadata mas adelante
    @Embedded
    public AuditMetadata AuditMetadata; // O dividir en strings


    public Client(String tenantId, DocumentoIdentidad documentoIdentidad, NombrePersona nombrePersona, String email,
            String telefono, ClientStatus clientStatus, String genero,
            Date fechaNacimiento, Address address, String internalNotes,
            String metadata, AuditMetadata auditMetadata) {
        
        TenantId = tenantId;
        DocumentoIdentidad = documentoIdentidad;
        NombrePersona = nombrePersona;
        Email = email;
        Telefono = telefono;
        ClientStatus = clientStatus;
        Genero = genero;
        FechaNacimiento = fechaNacimiento;
        Address = address;
        InternalNotes = internalNotes;
        Metadata = metadata;
        AuditMetadata = auditMetadata;
    }
  /**
     * Método de fábrica estático para instanciar contratos u órdenes.
     *
     * @param TenantId Id del tenant.
     * @param DocumentoIdentidad Número de documento de identidad.
     * @param NombrePersona Nombre de la persona.
     * @param Email Email de la persona.
     * @param Telefono Teléfono de la persona.
     * @param ClientStatus Estado del cliente (activo/inactivo).
     * @param Genero Género de la persona.
     * @param FechaNacimiento Fecha de nacimiento de la persona.
     * @param Address Dirección de la persona.
     * @param InternalNotes Notas internas.
     * @param Metadata Metadatos.
     * @param AuditMetadata Metadatos de auditoría.
     * @return Nueva instancia de {@link Client}.
     */
    public static Client create(
            String tenantId, 
            DocumentoIdentidad documentoIdentidad, 
            NombrePersona nombrePersona, 
            String email,
            String telefono, 
            ClientStatus clientStatus, 
            String genero,
            Date fechaNacimiento, 
            Address address, 
            String internalNotes,
            String metadata, 
            AuditMetadata auditMetadata
    ) {
        return new Client( tenantId, 
            documentoIdentidad, 
            nombrePersona, 
            email, 
            telefono, 
            clientStatus, 
            genero, 
            fechaNacimiento, 
            address, 
            internalNotes, 
            metadata, 
            auditMetadata
        );
    }
  public Long getId() {
    return id;
  }
  public String getTenantId() {
    return TenantId;
  }
  public DocumentoIdentidad getDocumentoIdentidad() {
    return DocumentoIdentidad;
  }
  public NombrePersona getNombrePersona() {
    return NombrePersona;
  }
  public String getEmail() {
    return Email;
  }
  public String getTelefono() {
    return Telefono;
  }
  public ClientStatus getClientStatus() {
    return ClientStatus;
  }
  public String getGenero() {
    return Genero;
  }
  public Date getFechaNacimiento() {
    return FechaNacimiento;
  }
  public Address getAddress() {
    return Address;
  }
  public String getInternalNotes() {
    return InternalNotes;
  }
  public String getMetadata() {
    return Metadata;
  }
  public AuditMetadata getAuditMetadata() {
    return AuditMetadata;
  }
}
