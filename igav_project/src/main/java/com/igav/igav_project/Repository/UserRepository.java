package com.igav.igav_project.Repository;

import com.igav.igav_project.Model.Entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la gestión de usuarios y roles del sistema (RF-02).
 *
 * @author IGAV Development Team
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su dirección de email.
     *
     * @param email Email a buscar.
     * @return Optional con el usuario encontrado.
     */
    Optional<User> findByEmailEmail(String email);

    /**
    * Obtiene los usuarios asignados a una sucursal (RF-02).
     *
     * @param branchId ID de la sucursal.
     * @return Lista de usuarios asignados a la sucursal.
     */
    List<User> findByBranchesId(Long branchId);
}

