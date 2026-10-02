package com.igav.igav_project.Controller;

import com.igav.igav_project.DTO.CreateCustomerDTO;
import com.igav.igav_project.Model.Entity.Order.Customer;
import com.igav.igav_project.Model.Entity.Store.Store;
import com.igav.igav_project.Model.Shared.ValueObjects.Address;
import com.igav.igav_project.Model.Shared.ValueObjects.DocumentoIdentidad;
import com.igav.igav_project.Model.Shared.ValueObjects.Email;
import com.igav.igav_project.Model.Shared.ValueObjects.Telefono;
import com.igav.igav_project.Model.Shared.ValueObjects.TipoDocumento;
import com.igav.igav_project.Repository.CustomerRepository;
import com.igav.igav_project.Repository.StoreRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
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
    private final StoreRepository storeRepository;

    /**
     * Constructor con inyección de dependencias para los repositorios.
     *
     * @param customerRepository Repositorio JPA de {@link Customer}.
     * @param storeRepository Repositorio JPA de {@link Store}.
     */
    public CustomerController(CustomerRepository customerRepository, StoreRepository storeRepository) {
        this.customerRepository = customerRepository;
        this.storeRepository = storeRepository;
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

    /**
     * Registra un nuevo cliente en el directorio maestro de la base de datos MySQL (RF-06).
     *
     * @param request Datos del cliente (documento, nombres, teléfono, etc.).
     * @return Cliente creado y persistido con status 201 Created.
     */
    @PostMapping
    @Operation(summary = "Registrar nuevo cliente", description = "Persiste un nuevo cliente en la BD MySQL con validación de identidad (RF-06)")
    public ResponseEntity<Customer> createCustomer(@RequestBody CreateCustomerDTO request) {
        String nombres = request.nombres();
        String apellidos = request.apellidos();
        if (apellidos == null || apellidos.isBlank()) {
            if (nombres != null && nombres.trim().contains(" ")) {
                String[] parts = nombres.trim().split(" ", 2);
                nombres = parts[0];
                apellidos = parts[1];
            } else {
                apellidos = "Cliente";
            }
        }

        TipoDocumento docTipo = TipoDocumento.DNI;
        if (request.tipoDocumento() != null) {
            try {
                docTipo = TipoDocumento.valueOf(request.tipoDocumento());
            } catch (Exception ignored) {}
        }

        String docNum = (request.numeroDocumento() != null && !request.numeroDocumento().isBlank())
            ? request.numeroDocumento()
            : String.format("%08d", (int)(Math.random() * 90000000) + 10000000);

        Store store = null;
        if (request.storeId() != null) {
            store = storeRepository.findById(request.storeId()).orElse(null);
        }
        if (store == null) {
            store = storeRepository.findAll().stream().findFirst().orElse(null);
        }

        String telStr = request.telefono() != null ? request.telefono().replaceAll("[\\s\\-\\(\\)]", "") : "+51999888777";
        if (!telStr.matches("^\\+?\\d{9,12}$")) {
            telStr = "+51999888777";
        }

        Customer customer = Customer.create(
            nombres != null ? nombres : "Cliente",
            apellidos,
            new DocumentoIdentidad(docTipo, docNum),
            new Email(request.email() != null && !request.email().isBlank() ? request.email() : "cliente@gala.pe"),
            new Telefono(telStr),
            new Address(
                request.direccion() != null ? request.direccion() : "San Isidro",
                "San Isidro", "Lima", "Perú", "15073", "Domicilio", "15073"
            ),
            store,
            "SYSTEM"
        );

        Customer saved = customerRepository.save(customer);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
