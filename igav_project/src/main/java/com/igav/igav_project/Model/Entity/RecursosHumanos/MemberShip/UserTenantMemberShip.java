
package com.igav.igav_project.Model.Entity.RecursosHumanos.MemberShip;

import java.time.LocalDateTime;

import com.igav.igav_project.Model.Entity.Organization.Branch.Branch;
import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;
import com.igav.igav_project.Model.Entity.RecursosHumanos.Employee.Employee;
import com.igav.igav_project.Model.Entity.RecursosHumanos.Role.Role;
import com.igav.igav_project.Model.Entity.User.User;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table (name = "user_tenant_member_ships")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class UserTenantMemberShip {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @Column(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id") // Opcional (EmployeeId?)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "default_branch_id", nullable = false)
    private Branch defaultBranch;

    @Column(name = "invited_by", nullable = false)
    private String invitedBy;

    @Embedded
    private AuditMetadata auditMetadata;

    @Column(name = "pin_hash", length = 255, nullable = false)
    private String pinHash;

    @Column(name = "pin_set_at", nullable = false)
    private LocalDateTime pinSetAt;

}
