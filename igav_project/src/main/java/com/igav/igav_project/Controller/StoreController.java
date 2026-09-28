package com.igav.igav_project.Controller;

import com.igav.igav_project.Model.Entity.Store.Store;
import com.igav.igav_project.Repository.StoreRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stores")
@Tag(name = "Gestión de Tiendas (Multi-tenant)", description = "Endpoints para administración de sedes y tiendas (RF-01)")
public class StoreController {

    private final StoreRepository storeRepository;

    public StoreController(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @GetMapping
    @Operation(summary = "Listar todas las tiendas", description = "Obtiene las sedes/tiendas registradas en la plataforma SaaS")
    public ResponseEntity<List<Store>> getAllStores() {
        return ResponseEntity.ok(storeRepository.findAll());
    }
}
