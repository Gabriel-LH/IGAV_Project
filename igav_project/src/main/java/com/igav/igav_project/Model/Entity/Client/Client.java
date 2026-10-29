package com.igav.igav_project.Model.Entity.Client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.igav.igav_project.Model.Entity.Commerce.Rental.Rental;
import com.igav.igav_project.Model.Entity.Commerce.Reservation.Reservation;
import com.igav.igav_project.Model.Entity.Commerce.Sale.Sale;
import com.igav.igav_project.Model.Shared.ValueObjects.*;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table(name = "clients")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "tenant_id", nullable = false, length = 50)
    public String tenantId;
    
    @Embedded
    @AttributeOverrides({
      @AttributeOverride(name = "numero", column = @Column(name = "document_id", nullable = false, length = 50)),
      @AttributeOverride(name = "tipo", column = @Column(name = "document_type", nullable = false, length = 50))
    })
    public DocumentoIdentidad documentoIdentidad;
    @Embedded
    @AttributeOverrides({
      @AttributeOverride(name = "tipo", column = @Column(name = "person_type", nullable = false, length = 50)),
      @AttributeOverride(name = "nombres", column = @Column(name = "first_names", length = 100)),
      @AttributeOverride(name = "apellidos", column = @Column(name = "last_names", length = 100)),
      @AttributeOverride(name = "razonSocial", column = @Column(name = "business_name", length = 150))
    })
    public NombrePersona nombrePersona;
    @Column (name = "email", nullable = false, length = 100)
    public String email;
    @Column (name = "phone", nullable = true, length = 20)
    public String telefono;
    @Enumerated (EnumType.STRING)
    @Column (name = "status", nullable = false)
    public ClientStatus clientStatus;
    @Column (name = "gender", nullable = true, length = 20)
    public String genero;
    @Column (name = "birth_date", nullable = true)
    public Date fechaNacimiento;
    @Embedded
    public Address address; // O dividir en strings
    @Column (name = "internal_notes", nullable = true, length = 255)
    public String internalNotes;
    @Column (name = "metadata", nullable = true, columnDefinition = "TEXT")
    @Builder.Default
    public String metadata = "{}"; //Posible metadata mas adelante
    @Embedded
    public AuditMetadata auditMetadata; // O dividir en strings

    @OneToMany (mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Reservation> reservations = new ArrayList<>();
    @OneToMany (mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Rental> rentals = new ArrayList<>();
    @OneToMany (mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Sale> sales = new ArrayList<>();
    
    

  /**
     * Método de fábrica estático para instanciar contratos u órdenes.
     *
     * @param tenantId Id del tenant.
     * @param documentoIdentidad Número de documento de identidad.
     * @param nombrePersona Nombre de la persona.
     * @param email Email de la persona.
     * @param telefono Teléfono de la persona.
     * @param clientStatus Estado del cliente (activo/inactivo).
     * @param genero Género de la persona.
     * @param fechaNacimiento Fecha de nacimiento de la persona.
     * @param address Dirección de la persona.
     * @param internalNotes Notas internas.
     * @param metadata Metadatos.
     * @param auditMetadata Metadatos de auditoría.
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



  public Client(String tenantId2, DocumentoIdentidad documentoIdentidad2, NombrePersona nombrePersona2, String email2,
        String telefono2, ClientStatus clientStatus2, String genero2, Date fechaNacimiento2, Address address2,
        String internalNotes2, String metadata2, AuditMetadata auditMetadata2) {
  }

}
