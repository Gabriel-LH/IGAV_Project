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
     * Obtiene los usuarios pertenecientes a una tienda específica (Aislamiento por Tenant, RF-01).
     *
     * @param storeId ID de la tienda.
     * @return Lista de usuarios de la tienda.
     */
    List<User> findByStoreId(Long storeId);
}

