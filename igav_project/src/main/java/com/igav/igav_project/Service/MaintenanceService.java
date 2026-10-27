package com.igav.igav_project.Service;

import com.igav.igav_project.Exception.ResourceNotFoundException;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalItemStatus;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalReturnInspection;
import com.igav.igav_project.Model.Entity.Commerce.Rental.ReturnDisposition;
import com.igav.igav_project.Model.Entity.Inventory.InventoryItem.InventoryCondition;
import com.igav.igav_project.Model.Entity.Inventory.InventoryItem.InventoryStatus;
import com.igav.igav_project.Repository.InventoryRepository;
import com.igav.igav_project.Repository.RentalReturnInspectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Servicio de negocio para gestionar la finalización del proceso de lavandería/tintorería y liberación de prendas.
 * Cumple con los requerimientos RF-05 y RF-11.
 *
 * @author IGAV Development Team
 */
@Service
public class MaintenanceService {

    private final RentalReturnInspectionRepository returnInspectionRepository;
    private final InventoryRepository inventoryRepository;

    public MaintenanceService(
            RentalReturnInspectionRepository returnInspectionRepository,
            InventoryRepository inventoryRepository
    ) {
        this.returnInspectionRepository = returnInspectionRepository;
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Completa el proceso de tintorería/lavado de una prenda y la marca como DISPONIBLE nuevamente (RF-05, RF-11).
     *
    * @param inspectionId ID de la inspección de devolución.
     * @param updatedBy Usuario responsable del alta técnica.
     * @return Inspección actualizada y prenda liberada.
     */
    @Transactional
    public RentalReturnInspection completeMaintenance(Long inspectionId, String updatedBy) {
        Long id = inspectionId;
        if (id == null) {
            throw new IllegalArgumentException("El ID de inspección es obligatorio.");
        }
        RentalReturnInspection inspection = returnInspectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inspección de devolución no encontrada con ID: " + inspectionId));
        if (inspection.getDisposition() != ReturnDisposition.LAVANDERIA
                && inspection.getDisposition() != ReturnDisposition.MANTENIMIENTO) {
            throw new IllegalStateException("La inspección no tiene un proceso de lavandería o mantenimiento pendiente.");
        }
        if (inspection.getCompletedAt() != null) {
            throw new IllegalStateException("Esta inspección ya fue completada.");
        }

        var inventoryItem = inventoryRepository.findByIdForUpdate(inspection.getInventoryItem().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Prenda no encontrada: " + inspection.getInventoryItem().getId()));
                InventoryStatus expectedStatus = inspection.getDisposition() == ReturnDisposition.MANTENIMIENTO
                    ? InventoryStatus.EnMantenimiento
                    : InventoryStatus.EnLavanderia;
                if (inventoryItem.getInventoryStatus() != expectedStatus) {
                    throw new IllegalStateException("La prenda ya no está en el estado esperado para completar el proceso.");
                }
        if (inspection.getDisposition() == ReturnDisposition.MANTENIMIENTO) {
            inventoryItem.setInventoryCondition(InventoryCondition.Reacondicionado);
        }
        inventoryItem.setInventoryStatus(InventoryStatus.Disponible);
        inventoryItem.setAvailableAt(null);
        inventoryItem.setLastMaintance(LocalDate.now());
        inspection.getRentalItem().setStatus(RentalItemStatus.Devuelto);
        inspection.setCompletedAt(LocalDateTime.now());
        inspection.setCompletedBy(updatedBy);
        inspection.setAuditMetadata(inspection.getAuditMetadata().update(updatedBy));
        inventoryRepository.save(inventoryItem);
        return returnInspectionRepository.save(inspection);
    }
}
