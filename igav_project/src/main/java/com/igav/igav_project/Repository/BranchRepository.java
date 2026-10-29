package com.igav.igav_project.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.igav.igav_project.Model.Entity.Organization.Branch.Branch;
import com.igav.igav_project.Model.Entity.Organization.Branch.BranchStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la gestión de Tiendas suscriptoras (SaaS Multi-tenant, RF-01).
 *
 * @author IGAV Development Team
 */
public interface BranchRepository extends JpaRepository<Branch, Long> {

    /**
     * Busca una tienda por el número de RUC.
     *
     * @param code Número de RUC.
     * @return Optional con la tienda si existe.
     */
    Optional<Branch> findByCode(String code);

    /**
     * Lista tiendas por estado de suscripción.
     *
     * @param status Estado de la tienda.
     * @return Lista de tiendas.
     */
    List<Branch> findByStatus(BranchStatus status);
}

