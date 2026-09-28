package com.igav.igav_project.Controller;

import com.igav.igav_project.DTO.CreateOrderRequestDTO;
import com.igav.igav_project.DTO.OrderResponseDTO;
import com.igav.igav_project.DTO.ReturnOrderRequestDTO;
import com.igav.igav_project.Service.OrderService;
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
@RequestMapping("/api/v1/orders")
@Tag(name = "Gestión de Contratos y Alquileres", description = "Endpoints para reservas, detección de colisión de fechas y liquidación de garantías")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Endpoint para registrar un nuevo contrato de alquiler u orden de venta (RF-04, RF-06, RF-08).
     *
     * @param request Datos de la orden.
     * @return DTO de la orden procesada.
     */
    @PostMapping
    @Operation(summary = "Crear contrato u orden", description = "Valida colisión de fechas (RF-04), calcula importes de alquiler y depósitos de garantía (RF-06, RF-08)")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody CreateOrderRequestDTO request) {
        OrderResponseDTO response = orderService.createOrder(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint para procesar la devolución física de una orden de alquiler (RF-05, RF-08, RF-11, RF-12, RF-13).
     *
     * @param request Datos de la devolución e incidencias.
     * @return DTO de la orden actualizada.
     */
    @PostMapping("/return")
    @Operation(summary = "Procesar devolución de contrato", description = "Registra incidencias por daños/retrasos, descuenta del depósito de garantía (RF-08, RF-13) y genera el bloqueo de tintorería de 24-48h (RF-05)")
    public ResponseEntity<OrderResponseDTO> processReturn(@RequestBody ReturnOrderRequestDTO request) {
        OrderResponseDTO response = orderService.processOrderReturn(request);
        return ResponseEntity.ok(response);
    }
}
