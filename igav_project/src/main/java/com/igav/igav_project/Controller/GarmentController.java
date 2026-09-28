package com.igav.igav_project.Controller;

import com.igav.igav_project.Model.Entity.Inventory.Garment;
import com.igav.igav_project.Model.Entity.Inventory.GarmentStatus;
import com.igav.igav_project.Service.GarmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para administrar el inventario y catálogo de prendas (RF-01, RF-03, RF-11, RF-12).
 *
 * @author IGAV Development Team
 */
@RestController
@RequestMapping("/api/v1/garments")
@Tag(name = "Gestión de Inventario y Catálogo", description = "Endpoints para registro, consulta y control de prendas")
public class GarmentController {

    private final GarmentService garmentService;

    public GarmentController(GarmentService garmentService) {
        this.garmentService = garmentService;
    }

    /**
     * Endpoint para consultar las prendas de una tienda específica (Aislamiento Multi-tenant, RF-01, RF-03).
     *
     * @param storeId ID de la tienda.
     * @return Lista de prendas.
     */
    @GetMapping("/store/{storeId}")
    @Operation(summary = "Listar prendas por tienda", description = "Obtiene el catálogo de prendas pertenecientes al tenant/tienda especificado (RF-01, RF-03)")
    public ResponseEntity<List<Garment>> getGarmentsByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(garmentService.getGarmentsByStore(storeId));
    }

    /**
     * Endpoint para filtrar prendas por estado de inventario (RF-11).
     *
     * @param storeId ID de la tienda.
     * @param estado Estado de la prenda (DISPONIBLE, ALQUILADO, EN_TINTORERIA, etc.).
     * @return Lista de prendas filtradas.
     */
    @GetMapping("/store/{storeId}/status/{estado}")
    @Operation(summary = "Listar prendas por estado", description = "Filtra el inventario por estado actual de la prenda (RF-11)")
    public ResponseEntity<List<Garment>> getGarmentsByStatus(
            @PathVariable Long storeId,
            @PathVariable GarmentStatus estado
    ) {
        return ResponseEntity.ok(garmentService.getGarmentsByStoreAndStatus(storeId, estado));
    }

    /**
     * Endpoint para obtener prendas con alerta por exceso de usos acumulados (RF-12).
     *
     * @param storeId ID de la tienda.
     * @return Lista de prendas que superan el umbral de rotación.
     */
    @GetMapping("/store/{storeId}/rotation-alerts")
    @Operation(summary = "Alertas de rotación de uso", description = "Obtiene prendas que superan el umbral máximo sugerido de alquileres/lavados (RF-12)")
    public ResponseEntity<List<Garment>> getRotationAlerts(@PathVariable Long storeId) {
        return ResponseEntity.ok(garmentService.getGarmentsExceedingRotation(storeId));
    }
}
