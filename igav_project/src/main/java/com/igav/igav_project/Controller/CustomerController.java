package com.igav.igav_project.Controller;

import com.igav.igav_project.Model.Entity.Order.Customer;
import com.igav.igav_project.Repository.CustomerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión y consulta del directorio maestro de clientes (RF-06).
 * Provee endpoints para la administración multi-tenant de clientes corporativos y particulares de gala.
 *
 * @author IGAV Development Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/customers")
@Tag(name = "Gestión de Clientes", description = "Endpoints para consulta y registro de clientes finales (RF-06)")
public class CustomerController {

    private final CustomerRepository customerRepository;

    /**
     * Constructor con inyección de dependencias para el repositorio de clientes.
     *
     * @param customerRepository Repositorio JPA de {@link Customer}.
     */
    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Obtiene el listado completo de todos los clientes registrados en la plataforma.
     *
     * @return Lista de clientes registrados con estado HTTP 200 OK.
     */
    @GetMapping
    @Operation(summary = "Listar todos los clientes", description = "Obtiene el directorio completo de clientes registrados en el sistema")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerRepository.findAll());
    }

    /**
     * Obtiene el listado de clientes filtrados por el identificador único de la sede o tienda.
     *
     * @param storeId Identificador numérico de la tienda.
     * @return Lista de clientes de la tienda especificada.
     */
    @GetMapping("/store/{storeId}")
    @Operation(summary = "Listar clientes por tienda", description = "Obtiene los clientes pertenecientes a la tienda indicada")
    public ResponseEntity<List<Customer>> getCustomersByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(customerRepository.findByStoreId(storeId));
    }
}
