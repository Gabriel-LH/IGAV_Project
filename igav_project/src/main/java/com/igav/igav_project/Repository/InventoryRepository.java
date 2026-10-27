package com.igav.igav_project.Repository;

import com.igav.igav_project.Model.Entity.Inventory.InventoryItem.InventoryItem;

import com.igav.igav_project.Model.Entity.Inventory.InventoryItem.InventoryStatus;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
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

    Optional<InventoryItem> findBySerialCode(String serialCode);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT item FROM InventoryItem item WHERE item.id = :itemId")
    Optional<InventoryItem> findByIdForUpdate(@Param("itemId") Long itemId);

    /**
     * Lista las prendas pertenecientes a una tienda específica (Aislamiento Multi-tenant, RF-01).
     *
     * @param branchId ID de la tienda.
     * @return Lista de prendas de la tienda.
     */
    List<InventoryItem> findByBranchId(Long branchId);

    List<InventoryItem> findByBranchIdAndInventoryStatus(Long branchId, InventoryStatus inventoryStatus);
}

