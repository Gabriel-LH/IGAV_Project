package com.igav.igav_project.Controller;

import com.igav.igav_project.DTO.CreateUserDTO;
import com.igav.igav_project.Model.Entity.Organization.Branch.Branch;
import com.igav.igav_project.Model.Entity.User.GlobalRole;
import com.igav.igav_project.Model.Entity.User.StatusUser;
import com.igav.igav_project.Model.Entity.User.User;
import com.igav.igav_project.Model.Shared.ValueObjects.Address;
import com.igav.igav_project.Model.Shared.ValueObjects.DocumentoIdentidad;
import com.igav.igav_project.Model.Shared.ValueObjects.Email;
import com.igav.igav_project.Model.Shared.ValueObjects.Telefono;
import com.igav.igav_project.Model.Shared.ValueObjects.TipoDocumento;
import com.igav.igav_project.Repository.BranchRepository;
import com.igav.igav_project.Repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Controlador REST para la gestión y consulta de usuarios del sistema y asignación de roles (RF-02).
 *
 * @author IGAV Development Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Gestión de Usuarios & Roles", description = "Endpoints para administración de usuarios y permisos (RF-02)")
public class UserController {

    private final UserRepository userRepository;
    private final BranchRepository storeRepository;

    /**
     * Constructor para la inyección de repositorios.
     *
     * @param userRepository Repositorio JPA de {@link User}.
    * @param storeRepository Repositorio JPA de {@link Branch}.
     */
    public UserController(UserRepository userRepository, BranchRepository storeRepository) {
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
    }

    /**
     * Obtiene la nómina completa de usuarios del sistema con sus respectivos roles y estados.
     *
     * @return Lista de usuarios del sistema.
     */
    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Obtiene todos los usuarios del sistema")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    /**
     * Obtiene los usuarios pertenecientes al personal de una sede o tienda específica.
     *
     * @param storeId Identificador de la tienda.
     * @return Lista de usuarios asignados a la tienda.
     */
    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Listar usuarios por sucursal", description = "Obtiene los usuarios asignados a la sucursal indicada")
    public ResponseEntity<List<User>> getUsersByBranch(@PathVariable Long branchId) {
        return ResponseEntity.ok(userRepository.findByBranchesId(branchId));
    }

    /**
     * Registra un nuevo usuario del sistema o miembro de staff en la base de datos MySQL (RF-02).
     *
     * @param request Datos del nuevo usuario.
     * @return Usuario creado con status 201 Created.
     */
    @PostMapping
    @Operation(summary = "Crear nuevo usuario de staff", description = "Persiste un nuevo usuario con rol de seguridad en la BD (RF-02)")
    @SuppressWarnings("null")
    public ResponseEntity<User> createUser(@RequestBody CreateUserDTO request) {
        String nombres = request.nombres();
        String apellidos = request.apellidos();
        if (apellidos == null || apellidos.isBlank()) {
            if (nombres != null && nombres.trim().contains(" ")) {
                String[] parts = nombres.trim().split(" ", 2);
                nombres = parts[0];
                apellidos = parts[1];
            } else {
                apellidos = "Staff";
            }
        }

        GlobalRole role = GlobalRole.USER;
        if (request.rol() != null) {
            role = request.rol().equals("ADMIN_SAAS") || request.rol().equals("SUPER_ADMIN")
                    ? GlobalRole.SUPER_ADMIN
                    : GlobalRole.USER;
        }

        Branch branch = null;
        if (request.storeId() != null) {
            branch = storeRepository.findById(request.storeId())
                    .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con ID: " + request.storeId()));
        } else if (role != GlobalRole.SUPER_ADMIN) {
            throw new IllegalArgumentException("El ID de sucursal es obligatorio para usuarios de sucursal.");
        }

        if (request.numeroDocumento() == null || request.numeroDocumento().isBlank()) {
            throw new IllegalArgumentException("El número de documento es obligatorio.");
        }

        TipoDocumento docTipo = TipoDocumento.DNI;
        if (request.tipoDocumento() != null) {
            try {
                docTipo = TipoDocumento.valueOf(request.tipoDocumento());
            } catch (Exception ignored) {}
        }

        String telStr = request.telefono() != null ? request.telefono().replaceAll("[\\s\\-\\(\\)]", "") : "+51999888777";
        if (!telStr.matches("^\\+?\\d{9,12}$")) {
            telStr = "+51999888777";
        }

        User user = User.create(
            nombres != null ? nombres : "Staff",
            apellidos,
            new Email(request.email() != null ? request.email() : "staff@igav.pe"),
            new DocumentoIdentidad(docTipo, request.numeroDocumento()),
            "https://images.unsplash.com/photo-1534528741775-53994a69daeb?q=80&w=200",
            new Telefono(telStr),
            role,
            new Address("Av. Conquistadores 780", "San Isidro", "Lima", "Perú", "15073", "Oficina", "15073"),
            "SYSTEM"
        );

        if (branch != null) {
            user.setBranches(Set.of(branch));
        }

        User saved = userRepository.save(user);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * Alterna el estado activo / inactivo de un usuario.
     *
     * @param id ID del usuario.
     * @return Usuario con estado actualizado.
     */
    @PatchMapping("/{id}/toggle-status")
    @Operation(summary = "Cambiar estado de usuario", description = "Alterna el estado ACTIVO/INACTIVO del personal")
    @SuppressWarnings("null")
    public ResponseEntity<User> toggleUserStatus(@PathVariable Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        if (user.getStatusUser() == StatusUser.ACTIVO) {
            user.markAsInactive("SYSTEM");
        } else {
            user.markAsActive("SYSTEM");
        }
        return ResponseEntity.ok(userRepository.save(user));
    }
}
