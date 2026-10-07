package com.igav.igav_project.Model.Entity.Organization.Branch;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.igav.igav_project.Model.Entity.Commerce.Reservation.Reservation;
import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;
import com.igav.igav_project.Model.Entity.User.User;
import com.igav.igav_project.Model.Shared.ValueObjects.Address;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;
import com.igav.igav_project.Model.Shared.ValueObjects.Email;
import com.igav.igav_project.Model.Shared.ValueObjects.Telefono;

import jakarta.persistence.*;

@Entity 
@Table (name = "branches")
public class Branch {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn (name = "tenant_id", nullable = false)
    @ManyToOne (fetch = FetchType.LAZY)
    private Tenant tenant;
    @Column (name = "code", nullable = false, length = 100)
    private String code;
    @Column (name = "name", nullable = false, length = 100)
    private String name;
    @Column (name = "address", nullable = false, length = 255)
    @Embedded 
    private Address address;
    @Column (name = "telefono", nullable = false)
    @Embedded 
    private Telefono telefono;
    @Column  (name = "email", nullable = false, length = 100)
    @Embedded 
    private Email email;
    @Column (name = "timezone", nullable = false, length = 100)
    private String timezone;
    @Column (name = "is_primary", nullable = false)
    private boolean isPrimary;
    @Column (name = "status", nullable = false)
    private StatusBranch status;

    @Column (name = "audit_metadata", nullable = false)
    private AuditMetadata auditMetadata;

    @ManyToMany(mappedBy = "branches")
    private Set<User> users = new HashSet<>();

    @OneToMany (mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;
    
    public Branch() {
    }

    public Branch(Long id, Tenant tenant, String code, String name, Address address, Telefono telefono, Email email,
            String timezone, boolean isPrimary, StatusBranch status, AuditMetadata auditMetadata) {
        this.id = id;
        this.tenant = tenant;
        this.code = code;
        this.name = name;
        this.address = address;
        this.telefono = telefono;
        this.email = email;
        this.timezone = timezone;
        this.isPrimary = isPrimary;
        this.status = status;
        this.auditMetadata = auditMetadata;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Telefono getTelefono() {
        return telefono;
    }

    public void setTelefono(Telefono telefono) {
        this.telefono = telefono;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public StatusBranch getStatus() {
        return status;
    }

    public void setStatus(StatusBranch status) {
        this.status = status;
    }

    public AuditMetadata getAuditMetadata() {
        return auditMetadata;
    }

    public void setAuditMetadata(AuditMetadata auditMetadata) {
        this.auditMetadata = auditMetadata;
    }

    
}
