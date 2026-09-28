package com.igav.igav_project.Repository;

import com.igav.igav_project.Model.Entity.Store.Store;
import com.igav.igav_project.Model.Entity.Store.StoreStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la gestión de Tiendas suscriptoras (SaaS Multi-tenant, RF-01).
 *
 * @author IGAV Development Team
 */
@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    /**
     * Busca una tienda por el número de RUC.
     *
     * @param rucNumero Número de RUC.
     * @return Optional con la tienda si existe.
     */
    Optional<Store> findByRucNumero(String rucNumero);

    /**
     * Lista tiendas por estado de suscripción.
     *
     * @param status Estado de la tienda.
     * @return Lista de tiendas.
     */
    List<Store> findByStatus(StoreStatus status);
}
