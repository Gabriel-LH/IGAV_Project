package com.igav.igav_project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.igav.igav_project.Model.Entity.Catalog.Category.Category;

import java.util.List;

/**
 * Repositorio JPA para la administración del catálogo de categorías por tienda (RF-03).
 *
 * @author IGAV Development Team
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Obtiene las categorías de prendas registradas por una tienda.
     *
     * @param storeId ID de la tienda.
     * @return Lista de categorías.
     */
    List<Category> findByStoreId(Long storeId);
}

