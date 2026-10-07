package com.igav.igav_project.Model.Entity.Organization.Branch.BranchConfig;

import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;

@Entity 
@Table (name = "branch_configs")
public class BranchConfig {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="open_hours_config", nullable = true)
    @Embedded 
    private OpenHoursConfig openHoursConfig;
    @Column(name = "audit_metadata", nullable = false)
    @Embedded 
    private  AuditMetadata auditMetadata;
    public BranchConfig() {
    }
    public BranchConfig(Long id, OpenHoursConfig openHoursConfig, AuditMetadata auditMetadata) {
        this.id = id;
        this.openHoursConfig = openHoursConfig;
        this.auditMetadata = auditMetadata;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public OpenHoursConfig getOpenHoursConfig() {
        return openHoursConfig;
    }
    public void setOpenHoursConfig(OpenHoursConfig openHoursConfig) {
        this.openHoursConfig = openHoursConfig;
    }
    public AuditMetadata getAuditMetadata() {
        return auditMetadata;
    }
    public void setAuditMetadata(AuditMetadata auditMetadata) {
        this.auditMetadata = auditMetadata;
    }

    

}
