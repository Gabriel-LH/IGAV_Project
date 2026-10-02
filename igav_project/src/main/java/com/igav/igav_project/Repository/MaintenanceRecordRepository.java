package com.igav.igav_project.Repository;

import com.igav.igav_project.Model.Entity.Maintenance.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para el registro de ciclos de mantenimiento y tintorería (RF-05, RF-12).
 *
 * @author IGAV Development Team
 */
public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {

    /**
     * Obtiene el historial de mantenimientos/lavados de una prenda concreta.
     *
     * @param garmentId ID de la prenda.
     * @return Lista de registros de mantenimiento.
     */
    List<MaintenanceRecord> findByGarmentId(Long garmentId);

    /**
     * Verifica si una prenda tiene un mantenimiento activo en un rango de fechas (RF-05).
     *
     * @param garmentId ID de la prenda.
     * @param fechaEntrega Fecha de inicio.
     * @param fechaDevolucion Fecha de fin.
     * @return true si la prenda está bloqueada en tintorería.
     */
    @Query("SELECT COUNT(m) > 0 FROM MaintenanceRecord m " +
           "WHERE m.garment.id = :garmentId " +
           "AND m.fechaFinReal IS NULL " +
           "AND (m.fechaInicio < :fechaDevolucion AND m.fechaFinEstimada > :fechaEntrega)")
    boolean existsActiveMaintenanceInDateRange(
            @Param("garmentId") Long garmentId,
            @Param("fechaEntrega") LocalDateTime fechaEntrega,
            @Param("fechaDevolucion") LocalDateTime fechaDevolucion
    );
}

