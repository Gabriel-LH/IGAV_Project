package com.igav.igav_project.Model.Entity.RecursosHumanos.Employee;

import java.time.LocalDate;

import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;
import com.igav.igav_project.Model.Shared.ValueObjects.Address;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;
import com.igav.igav_project.Model.Shared.ValueObjects.DocumentoIdentidad;
import com.igav.igav_project.Model.Shared.ValueObjects.Email;
import com.igav.igav_project.Model.Shared.ValueObjects.Genero;
import com.igav.igav_project.Model.Shared.ValueObjects.NombrePersona;
import com.igav.igav_project.Model.Shared.ValueObjects.Telefono;
import com.igav.igav_project.Model.Shared.ValueObjects.TipoPersona;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table (name ="employees")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "tipo", column = @Column(name = "tipo_documento", nullable = false)),
        @AttributeOverride(name = "numero", column = @Column(name = "numero_documento", length = 20, nullable = false))
    })
    private DocumentoIdentidad documentoIdentidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_persona", nullable = false)
    private TipoPersona tipoPersona;

    @Embedded
    private NombrePersona nombrePersona;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "email", nullable = false))
    })
    private Email email;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "telefono", length = 20))
    })
    private Telefono telefono;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero", nullable = false)
    private Genero genero;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EmployeeStatus status;

    @Embedded
    private AuditMetadata auditMetadata;

}
