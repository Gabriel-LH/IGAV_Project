package com.igav.igav_project.Repository;

import com.igav.igav_project.Model.Entity.Inventory.InventoryItem.InventoryItem;

import com.igav.igav_project.Model.Entity.Organization.Branch.BranchStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la administración de prendas de inventario (RF-03, RF-11, RF-12).
 *
 * @author IGAV Development Team
 */
public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {

    /**
     * Busca una prenda por su código único de inventario SKU.
     *
     * @param codigoUnico Código SKU.
     * @return Optional con la prenda.
     */
    Optional<InventoryItem> findByCodigoUnico(String codigoUnico);

    /**
     * Lista las prendas pertenecientes a una tienda específica (Aislamiento Multi-tenant, RF-01).
     *
     * @param branchId ID de la tienda.
     * @return Lista de prendas de la tienda.
     */
    List<InventoryItem> findByBranchId(Long branchId);

    /**
     * Lista prendas por tienda y estado específico (RF-11).
     *
     * @param branchId ID de la tienda.
     * @param estado Estado de la prenda.
     * @return Lista de prendas.
     */
    List<InventoryItem> findByStoreIdAndEstado(Long branchId, BranchStatus estado);

    /**
     * Consulta prendas que han superado o alcanzado el umbral de alerta por rotación de usos (RF-12).
     *
     * @param branchId ID de la tienda.
     * @return Lista de prendas en riesgo de desgaste elevado.
     */
    @Query("SELECT g FROM InventryItem g WHERE g.branch.id = :branchId AND g.inventory_condition.Obsoleto")
    List<InventoryItem> findGarmentsExceedingMaxUses(@Param("storeId") Long storeId);
}

