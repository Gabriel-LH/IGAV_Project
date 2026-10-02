package com.igav.igav_project.Service;

import com.igav.igav_project.Exception.ResourceNotFoundException;
import com.igav.igav_project.Model.Entity.Inventory.Category;
import com.igav.igav_project.Model.Entity.Inventory.Garment;
import com.igav.igav_project.Model.Entity.Inventory.GarmentSize;
import com.igav.igav_project.Model.Entity.Inventory.GarmentStatus;
import com.igav.igav_project.Model.Entity.Store.Store;
import com.igav.igav_project.Repository.CategoryRepository;
import com.igav.igav_project.Repository.GarmentRepository;
import com.igav.igav_project.Repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Servicio de negocio para la administración del catálogo e inventario de prendas.
 * Cumple con los requerimientos RF-01, RF-03, RF-11 y RF-12.
 *
 * @author IGAV Development Team
 */
@Service
public class GarmentService {

    private final GarmentRepository garmentRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;

    public GarmentService(
            GarmentRepository garmentRepository,
            StoreRepository storeRepository,
            CategoryRepository categoryRepository
    ) {
        this.garmentRepository = garmentRepository;
        this.storeRepository = storeRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Registra una nueva prenda en el inventario de la tienda (RF-03).
     *
     * @param storeId ID de la tienda (Tenant).
     * @param categoryId ID de la categoría.
     * @param codigoUnico SKU único.
     * @param nombre Nombre descriptivo.
     * @param descripcion Descripción.
     * @param color Color.
     * @param talla Talla.
     * @param precioAlquiler Tarifa de alquiler.
     * @param precioVenta Precio de venta directa opcional.
     * @param depositoGarantia Depósito de garantía.
     * @param maxUsos Usos máximos recomendados antes de alerta.
     * @param horasTintoreria Horas de bloqueo en lavandería (24-48h).
     * @param createdBy Usuario creador.
     * @return Prenda creada.
     * @throws ResourceNotFoundException si la tienda o categoría no existen.
     */
    @Transactional
    public Garment createGarment(
            Long storeId,
            Long categoryId,
            String codigoUnico,
            String nombre,
            String descripcion,
            String color,
            GarmentSize talla,
            BigDecimal precioAlquiler,
            BigDecimal precioVenta,
            BigDecimal depositoGarantia,
            int maxUsos,
            int horasTintoreria,
            String createdBy
    ) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Tienda no encontrada con ID: " + storeId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + categoryId));

        Garment garment = Garment.create(
                codigoUnico, nombre, descripcion, color, talla, category, store,
                precioAlquiler, precioVenta, depositoGarantia, maxUsos, horasTintoreria, createdBy
        );

        return garmentRepository.save(garment);
    }

    /**
     * Obtiene una prenda por su ID.
     *
     * @param id ID de la prenda.
     * @return Instancia de {@link Garment}.
     */
    @Transactional(readOnly = true)
    public Garment getGarmentById(Long id) {
        return garmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prenda no encontrada con ID: " + id));
    }

    /**
     * Lista todas las prendas de una tienda (Multi-tenant, RF-01).
     *
     * @param storeId ID de la tienda.
     * @return Lista de prendas.
     */
    @Transactional(readOnly = true)
    public List<Garment> getGarmentsByStore(Long storeId) {
        return garmentRepository.findByStoreId(storeId);
    }

    /**
     * Obtiene las prendas de una tienda filtradas por estado (RF-11).
     *
     * @param storeId ID de la tienda.
     * @param estado Estado del ciclo de vida.
     * @return Lista de prendas.
     */
    @Transactional(readOnly = true)
    public List<Garment> getGarmentsByStoreAndStatus(Long storeId, GarmentStatus estado) {
        return garmentRepository.findByStoreIdAndEstado(storeId, estado);
    }

    /**
     * Obtiene prendas que han alcanzado su límite recomendado de rotación/uso (RF-12).
     *
     * @param storeId ID de la tienda.
     * @return Lista de prendas con alerta de rotación.
     */
    @Transactional(readOnly = true)
    public List<Garment> getGarmentsExceedingRotation(Long storeId) {
        return garmentRepository.findGarmentsExceedingMaxUses(storeId);
    }

    /**
     * Lista todas las prendas registradas en la plataforma de todas las sedes.
     *
     * @return Lista completa de prendas.
     */
    @Transactional(readOnly = true)
    public List<Garment> getAllGarments() {
        return garmentRepository.findAll();
    }
}
