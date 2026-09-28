package com.igav.igav_project.Controller;

import com.igav.igav_project.Model.Entity.Order.Customer;
import com.igav.igav_project.Repository.CustomerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@Tag(name = "Gestión de Clientes", description = "Endpoints para consulta y registro de clientes finales (RF-06)")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    @Operation(summary = "Listar todos los clientes", description = "Obtiene el directorio completo de clientes registrados")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerRepository.findAll());
    }

    @GetMapping("/store/{storeId}")
    @Operation(summary = "Listar clientes por tienda", description = "Obtiene los clientes pertenecientes a la tienda indicada")
    public ResponseEntity<List<Customer>> getCustomersByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(customerRepository.findByStoreId(storeId));
    }
}
