package com.igav.igav_project.Controller;

import com.igav.igav_project.DTO.CreateRentalRequestDTO;
import com.igav.igav_project.DTO.RentalResponseDTO;
import com.igav.igav_project.DTO.RentalReturnInspectionResponseDTO;
import com.igav.igav_project.DTO.RentalReturnRequestDTO;
import com.igav.igav_project.Service.RentalService;
import com.igav.igav_project.Service.RentalReturnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la gestión de Contratos de Alquiler, Ventas y Devoluciones con liquidación de garantías.
 * Cumple con los requerimientos RF-04, RF-05, RF-06, RF-08, RF-11, RF-12 y RF-13.
 *
 * @author IGAV Development Team
 */
@RestController
@RequestMapping("/api/v1/rentals")
@Tag(name = "Alquileres y devoluciones", description = "Gestión de alquileres, inspecciones, mora y garantías")
public class RentalController {

    private final RentalService rentalService;
    private final RentalReturnService rentalReturnService;

    public RentalController(RentalService rentalService, RentalReturnService rentalReturnService) {
        this.rentalService = rentalService;
        this.rentalReturnService = rentalReturnService;
    }

    /**
     * Endpoint para listar todos los contratos u órdenes registradas en la base de datos.
     *
     * @return Lista de contratos registrados.
     */
    @GetMapping
    @Operation(summary = "Listar todas las órdenes", description = "Obtiene el listado completo de contratos y alquileres registrados en la BD")
    public ResponseEntity<java.util.List<RentalResponseDTO>> getAllRentals() {
        return ResponseEntity.ok(rentalService.getAllRentals());
    }

    /**
     * Endpoint para listar los contratos pertenecientes a una tienda específica (Aislamiento por Tenant).
     *
     * @param storeId ID de la tienda.
     * @return Lista de contratos de la tienda.
     */
    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Listar alquileres por sucursal", description = "Obtiene los alquileres de la sucursal indicada")
    public ResponseEntity<java.util.List<RentalResponseDTO>> getRentalsByBranch(@PathVariable Long branchId) {
        return ResponseEntity.ok(rentalService.getRentalsByBranch(branchId));
    }

    /**
     * Endpoint para registrar un nuevo contrato de alquiler u orden de venta (RF-04, RF-06, RF-08).
     *
     * @param request Datos de la orden.
     * @return DTO de la orden procesada.
     */
    @PostMapping
    @Operation(summary = "Iniciar alquiler de una reserva", description = "Convierte una reserva confirmada en alquiler y rechaza prendas con fechas solapadas")
    public ResponseEntity<RentalResponseDTO> createRental(@RequestBody CreateRentalRequestDTO request) {
        RentalResponseDTO response = rentalService.createRental(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint para procesar la devolución física de una orden de alquiler (RF-05, RF-08, RF-11, RF-12, RF-13).
     *
     * @param request Datos de la devolución e incidencias.
     * @return DTO de la orden actualizada.
     */
    @PostMapping("/{rentalId}/returns")
    @Operation(summary = "Registrar devolución e inspección", description = "Inspecciona todos los ítems, calcula mora, aplica garantía y determina lavandería, mantenimiento o baja")
    public ResponseEntity<RentalResponseDTO> processReturn(
            @PathVariable Long rentalId,
            @RequestBody RentalReturnRequestDTO request) {
        RentalResponseDTO response = rentalReturnService.processReturn(rentalId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{rentalId}/returns")
    @Operation(summary = "Consultar inspecciones de devolución")
    public ResponseEntity<java.util.List<RentalReturnInspectionResponseDTO>> getReturnInspections(
            @PathVariable Long rentalId) {
        return ResponseEntity.ok(rentalReturnService.getReturnInspections(rentalId));
    }
}
