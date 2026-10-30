package com.igav.igav_project.Controller;

import com.igav.igav_project.DTO.CreateClientDTO;
import com.igav.igav_project.Model.Entity.Client.Client;
import com.igav.igav_project.Model.Entity.Organization.Branch.Branch;
import com.igav.igav_project.Model.Shared.ValueObjects.Address;
import com.igav.igav_project.Model.Shared.ValueObjects.DocumentoIdentidad;
import com.igav.igav_project.Model.Shared.ValueObjects.TipoDocumento;
import com.igav.igav_project.Model.Shared.ValueObjects.NombrePersona;
import com.igav.igav_project.Model.Entity.Client.ClientStatus;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;
import com.igav.igav_project.Repository.BranchRepository;
import com.igav.igav_project.Repository.ClientRepository;
import com.igav.igav_project.Service.ClientReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
public class ClientController {

    private final ClientRepository clientRepository;
    private final BranchRepository branchRepository;
    private final ClientReportService clientReportService;

    /**
     * Constructor con inyección de dependencias para los repositorios.
     *
     * @param clientRepository Repositorio JPA de {@link Client}.
     * @param branchRepository Repositorio JPA de {@link Branch}.
     */
    public ClientController(
            ClientRepository clientRepository,
            BranchRepository branchRepository,
            ClientReportService clientReportService) {
        this.clientRepository = clientRepository;
        this.branchRepository = branchRepository;
        this.clientReportService = clientReportService;
    }

    /**
     * Obtiene el listado completo de todos los clientes registrados en la plataforma.
     *
     * @return Lista de clientes registrados con estado HTTP 200 OK.
     */
    @GetMapping
    @Operation(summary = "Listar todos los clientes", description = "Obtiene el directorio completo de clientes registrados en el sistema")
    public ResponseEntity<List<Client>> getAllCustomers() {
        return ResponseEntity.ok(clientRepository.findAll());
    }

    /**
     * Descarga un reporte Excel con los datos básicos de todos los clientes.
     *
     * @return Archivo XLSX adjunto con el listado de clientes.
     * @throws IOException si ocurre un error al generar el archivo.
     */
    @GetMapping(value = "/report.xlsx", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @Operation(summary = "Exportar clientes a Excel", description = "Descarga el listado de clientes en formato XLSX")
    public ResponseEntity<byte[]> exportCustomersExcel() throws IOException {
        byte[] report = clientReportService.generateExcelReport();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=clientes.xlsx")
                .body(report);
    }

    /**
     * Obtiene el listado de clientes filtrados por el identificador único de la sede o tienda.
     *
     * @param storeId Identificador numérico de la tienda.
     * @return Lista de clientes de la tienda especificada.
     */
    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Listar clientes por tienda", description = "Obtiene los clientes pertenecientes a la tienda indicada")
    @SuppressWarnings("null")
    public ResponseEntity<List<Client>> getCustomersByStore(@PathVariable Long branchId) {
        Branch branch = branchRepository.findById(branchId)
            .orElseThrow(() -> new com.igav.igav_project.Exception.ResourceNotFoundException(
                "Sucursal no encontrada con ID: " + branchId));
        return ResponseEntity.ok(clientRepository.findByTenantId(branch.getTenant().getId().toString()));
    }

    /**
     * Registra un nuevo cliente en el directorio maestro de la base de datos MySQL (RF-06).
     *
     * @param request Datos del cliente (documento, nombres, teléfono, etc.).
     * @return Cliente creado y persistido con status 201 Created.
     */
    @PostMapping
    @Operation(summary = "Registrar nuevo cliente", description = "Persiste un nuevo cliente en la BD MySQL con validación de identidad (RF-06)")
    @SuppressWarnings("null")
    public ResponseEntity<Client> createCustomer(@RequestBody CreateClientDTO request) {
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

        if (request.numeroDocumento() == null || request.numeroDocumento().isBlank()) {
            throw new IllegalArgumentException("El número de documento es obligatorio.");
        }

        Long branchId = request.branchId();
        if (branchId == null) {
            throw new IllegalArgumentException("El ID de sucursal es obligatorio.");
        }
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new com.igav.igav_project.Exception.ResourceNotFoundException(
                        "Sucursal no encontrada con ID: " + branchId));
        String tenantId = branch.getTenant().getId().toString();
        if (clientRepository.findByDocumentoIdentidadNumeroAndTenantId(
                request.numeroDocumento(), tenantId).isPresent()) {
            throw new IllegalArgumentException("Ya existe un cliente con ese documento en el tenant.");
        }

        String telStr = request.telefono() != null ? request.telefono().replaceAll("[\\s\\-\\(\\)]", "") : "+51999888777";
        if (!telStr.matches("^\\+?\\d{9,12}$")) {
            telStr = "+51999888777";
        }

        Client client = Client.create(
            tenantId,
            new DocumentoIdentidad(docTipo, request.numeroDocumento()),
            NombrePersona.natural(nombres != null ? nombres : "Cliente", apellidos),
            request.email() != null && !request.email().isBlank() ? request.email() : "cliente@gala.pe",
            telStr,
            ClientStatus.ACTIVE,
            null,
            null,
            new Address(
                request.direccion() != null ? request.direccion() : "San Isidro",
                "San Isidro", "Lima", "Perú", "15073", "Domicilio", "15073"
            ),
            null,
            "{}",
            AuditMetadata.create("SYSTEM")
        );

        Client saved = clientRepository.save(client);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
