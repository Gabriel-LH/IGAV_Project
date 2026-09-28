package com.igav.igav_project.Controller;

import com.igav.igav_project.Model.Entity.User.User;
import com.igav.igav_project.Repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Gestión de Usuarios & Roles", description = "Endpoints para administración de usuarios y permisos (RF-02)")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Obtiene todos los usuarios del sistema")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/store/{storeId}")
    @Operation(summary = "Listar usuarios por tienda", description = "Obtiene los usuarios pertenecientes a la tienda indicada")
    public ResponseEntity<List<User>> getUsersByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(userRepository.findByStoreId(storeId));
    }
}
