package com.igav.igav_project.Controller;

import com.igav.igav_project.DTO.CreateBranchDTO;
import com.igav.igav_project.Model.Entity.Organization.Branch.Branch;
import com.igav.igav_project.Model.Shared.ValueObjects.Address;
import com.igav.igav_project.Model.Shared.ValueObjects.Email;
import com.igav.igav_project.Model.Shared.ValueObjects.Telefono;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;
import com.igav.igav_project.Repository.BranchRepository;
import com.igav.igav_project.Repository.TenantRepository;
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
@RequestMapping("/api/v1/branches")
@Tag(name = "Gestión de Tiendas (Multi-tenant)", description = "Endpoints para administración de sedes y tiendas (RF-01)")
public class BranchController {

    private final BranchRepository branchRepository;
    private final TenantRepository tenantRepository;

    /**
     * Constructor para la inyección del repositorio de tiendas.
     *
     * @param branchRepository Repositorio JPA de {@link Store}.
     */
    public BranchController(BranchRepository branchRepository, TenantRepository tenantRepository) {
        this.branchRepository = branchRepository;
        this.tenantRepository = tenantRepository;
    }

    /**
     * Obtiene todas las sedes y tiendas comerciales activas registradas en la plataforma.
     *
     * @return Lista de tiendas registradas con código tenant y dirección.
     */
    @GetMapping
    @Operation(summary = "Listar todas las tiendas", description = "Obtiene las sedes/tiendas registradas en la plataforma SaaS")
    public ResponseEntity<List<Branch>> getAllStores() {
        return ResponseEntity.ok(branchRepository.findAll());
    }

    /**
     * Registra una nueva tienda/sede en la plataforma SaaS (RF-01).
     *
     * @param request Datos de la tienda (razón social, nombre comercial, RUC, teléfono, dirección).
     * @return Tienda creada con status 201 Created.
     */
    @PostMapping
    @Operation(summary = "Registrar nueva tienda/sede", description = "Crea una nueva sede multi-tenant (RF-01)")
    @SuppressWarnings("null")
    public ResponseEntity<Branch> createBranch(@RequestBody CreateBranchDTO request) {
        if (request.tenantId() == null || request.code() == null || request.code().isBlank()
                || request.name() == null || request.name().isBlank()) {
            throw new IllegalArgumentException("Tenant, código y nombre de sucursal son obligatorios.");
        }
        var tenant = tenantRepository.findById(request.tenantId())
                .orElseThrow(() -> new com.igav.igav_project.Exception.ResourceNotFoundException(
                        "Tenant no encontrado con ID: " + request.tenantId()));
        String city = request.ciudad() != null ? request.ciudad() : "";
        Branch branch = Branch.builder()
                .tenant(tenant)
                .code(request.code())
                .name(request.name())
                .timezone(request.timezone() != null ? request.timezone() : "America/Lima")
                .address(new Address(
                        request.direccion() != null ? request.direccion() : "",
                        city, city, "Perú", "", "", ""))
                .email(new Email(request.email()))
                .telefono(new Telefono(request.telefono()))
                .isPrimary(request.primary())
                .status(com.igav.igav_project.Model.Entity.Organization.Branch.BranchStatus.ACTIVO)
                .auditMetadata(AuditMetadata.create("SYSTEM"))
                .build();

        Branch saved = branchRepository.save(branch);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
