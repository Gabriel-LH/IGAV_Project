package com.igav.igav_project.Repository;

import com.igav.igav_project.Model.Entity.Maintenance.ReturnIncident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para el registro de incidencias en la devolución de alquileres (RF-08, RF-13).
 *
 * @author IGAV Development Team
 */
@Repository
public interface ReturnIncidentRepository extends JpaRepository<ReturnIncident, Long> {

    /**
     * Obtiene las incidencias asociadas a un contrato u orden.
     *
     * @param orderId ID de la orden.
     * @return Lista de incidencias registradas.
     */
    List<ReturnIncident> findByOrderId(Long orderId);
}
