package com.igav.igav_project.Service;

import com.igav.igav_project.Exception.ResourceNotFoundException;
import com.igav.igav_project.Model.Entity.Inventory.Garment;
import com.igav.igav_project.Model.Entity.Maintenance.MaintenanceRecord;
import com.igav.igav_project.Repository.GarmentRepository;
import com.igav.igav_project.Repository.MaintenanceRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de negocio para gestionar la finalización del proceso de lavandería/tintorería y liberación de prendas.
 * Cumple con los requerimientos RF-05 y RF-11.
 *
 * @author IGAV Development Team
 */
@Service
public class MaintenanceService {

    private final MaintenanceRecordRepository maintenanceRecordRepository;
    private final GarmentRepository garmentRepository;

    public MaintenanceService(
            MaintenanceRecordRepository maintenanceRecordRepository,
            GarmentRepository garmentRepository
    ) {
        this.maintenanceRecordRepository = maintenanceRecordRepository;
        this.garmentRepository = garmentRepository;
    }

    /**
     * Completa el proceso de tintorería/lavado de una prenda y la marca como DISPONIBLE nuevamente (RF-05, RF-11).
     *
     * @param maintenanceRecordId ID del registro de mantenimiento.
     * @param updatedBy Usuario responsable del alta técnica.
     * @return Instancia actualizada de {@link MaintenanceRecord}.
     */
    @Transactional
    public MaintenanceRecord completeMaintenance(Long maintenanceRecordId, String updatedBy) {
        MaintenanceRecord record = maintenanceRecordRepository.findById(maintenanceRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de mantenimiento no encontrado con ID: " + maintenanceRecordId));

        record.finalizarMantenimiento(updatedBy);
        Garment garment = record.getGarment();
        garmentRepository.save(garment);

        return maintenanceRecordRepository.save(record);
    }
}
