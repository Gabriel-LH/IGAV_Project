package com.igav.igav_project.Model.Entity.Organization.Branch.BranchConfig;

import java.sql.Timestamp;

import jakarta.persistence.Embeddable;

@Embeddable 
public record DaySchedule(
    String Day,
    boolean Enabled,
    Timestamp Open,
    Timestamp Close
) {

}
