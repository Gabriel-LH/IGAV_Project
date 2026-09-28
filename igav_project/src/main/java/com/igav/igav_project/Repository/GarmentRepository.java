package com.igav.igav_project.Repository;

import com.igav.igav_project.Model.Entity.Inventory.Garment;
import com.igav.igav_project.Model.Entity.Inventory.GarmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la administración de prendas de inventario (RF-03, RF-11, RF-12).
 *
 * @author IGAV Development Team
 */
@Repository
public interface GarmentRepository extends JpaRepository<Garment, Long> {

    /**
     * Busca una prenda por su código único de inventario SKU.
     *
     * @param codigoUnico Código SKU.
     * @return Optional con la prenda.
     */
    Optional<Garment> findByCodigoUnico(String codigoUnico);

    /**
     * Lista las prendas pertenecientes a una tienda específica (Aislamiento Multi-tenant, RF-01).
     *
     * @param storeId ID de la tienda.
     * @return Lista de prendas de la tienda.
     */
    List<Garment> findByStoreId(Long storeId);

    /**
     * Lista prendas por tienda y estado específico (RF-11).
     *
     * @param storeId ID de la tienda.
     * @param estado Estado de la prenda.
     * @return Lista de prendas.
     */
    List<Garment> findByStoreIdAndEstado(Long storeId, GarmentStatus estado);

    /**
     * Consulta prendas que han superado o alcanzado el umbral de alerta por rotación de usos (RF-12).
     *
     * @param storeId ID de la tienda.
     * @return Lista de prendas en riesgo de desgaste elevado.
     */
    @Query("SELECT g FROM Garment g WHERE g.store.id = :storeId AND g.usosAcumulados >= g.maxUsosRecomendados")
    List<Garment> findGarmentsExceedingMaxUses(@Param("storeId") Long storeId);
}
