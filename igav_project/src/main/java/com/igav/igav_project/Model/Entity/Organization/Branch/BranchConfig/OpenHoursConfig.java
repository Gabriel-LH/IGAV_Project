package com.igav.igav_project.Model.Entity.Organization.Branch.BranchConfig;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable 
public record OpenHoursConfig(
    LocalTime startTime,
    LocalTime endTime,
    @Embedded 
    List<DaySchedule> horario

) {
    // Constructor compacto para asignar valores por defecto
    public OpenHoursConfig {
        // Hora de apertura por defecto: 08:00 AM
        if (startTime == null) {
            startTime = LocalTime.of(8, 0); 
        }
        
        // Hora de cierre por defecto: 06:00 PM (18:00)
        if (endTime == null) {
            endTime = LocalTime.of(18, 0); 
        }
        
        // Lista vacía por defecto para evitar nulos
        if (horario == null) {
            horario = new ArrayList<>();
        }
    }

}
