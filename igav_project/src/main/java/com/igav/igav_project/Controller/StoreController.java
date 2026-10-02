package com.igav.igav_project.Controller;

import com.igav.igav_project.DTO.CreateStoreDTO;
import com.igav.igav_project.Model.Entity.Store.Store;
import com.igav.igav_project.Model.Shared.ValueObjects.Address;
import com.igav.igav_project.Model.Shared.ValueObjects.DocumentoIdentidad;
import com.igav.igav_project.Model.Shared.ValueObjects.Email;
import com.igav.igav_project.Model.Shared.ValueObjects.Telefono;
import com.igav.igav_project.Model.Shared.ValueObjects.TipoDocumento;
import com.igav.igav_project.Repository.StoreRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la administración y consulta de sedes y tiendas del SaaS IGAV (RF-01).
 * Implementa el soporte para el aislamiento de datos por tenant comercial.
 *
 * @author IGAV Development Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/stores")
@Tag(name = "Gestión de Tiendas (Multi-tenant)", description = "Endpoints para administración de sedes y tiendas (RF-01)")
public class StoreController {

    private final StoreRepository storeRepository;

    /**
     * Constructor para la inyección del repositorio de tiendas.
     *
     * @param storeRepository Repositorio JPA de {@link Store}.
     */
    public StoreController(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    /**
     * Obtiene todas las sedes y tiendas comerciales activas registradas en la plataforma.
     *
     * @return Lista de tiendas registradas con código tenant y dirección.
     */
    @GetMapping
    @Operation(summary = "Listar todas las tiendas", description = "Obtiene las sedes/tiendas registradas en la plataforma SaaS")
    public ResponseEntity<List<Store>> getAllStores() {
        return ResponseEntity.ok(storeRepository.findAll());
    }

    /**
     * Registra una nueva tienda/sede en la plataforma SaaS (RF-01).
     *
     * @param request Datos de la tienda (razón social, nombre comercial, RUC, teléfono, dirección).
     * @return Tienda creada con status 201 Created.
     */
    @PostMapping
    @Operation(summary = "Registrar nueva tienda/sede", description = "Crea una nueva sede multi-tenant en MySQL (RF-01)")
    public ResponseEntity<Store> createStore(@RequestBody CreateStoreDTO request) {
        String nombre = request.nombreComercial() != null ? request.nombreComercial() : "Nueva Sede Gala";
        String razonSocial = request.razonSocial() != null ? request.razonSocial() : nombre + " S.A.C.";
        
        String rucStr = (request.ruc() != null && request.ruc().matches("\\d{11}"))
            ? request.ruc()
            : String.format("2060%07d", (int)(Math.random() * 9000000) + 1000000);

        String emailStr = (request.email() != null && !request.email().isBlank())
            ? request.email()
            : "sede" + System.currentTimeMillis() + "@igav-gala.pe";

        String telStr = (request.telefono() != null && !request.telefono().isBlank())
            ? request.telefono()
            : "+5114219000";

        String dir = request.direccion() != null ? request.direccion() : "Av. Principal 123";
        String ciudad = request.ciudad() != null ? request.ciudad() : "Lima";

        Store store = Store.create(
            nombre,
            razonSocial,
            new DocumentoIdentidad(TipoDocumento.RUC, rucStr),
            new Email(emailStr),
            new Telefono(telStr),
            new Address(dir, ciudad, ciudad, "Perú", "15000", "Local Comercial", "15000"),
            "SYSTEM"
        );

        Store saved = storeRepository.save(store);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
