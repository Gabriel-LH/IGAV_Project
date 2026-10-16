package com.igav.igav_project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.igav.igav_project.Model.Entity.Client.Client;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la gestión de clientes finales (RF-06).
 *
 * @author IGAV Development Team
 */
public interface CustomerRepository extends JpaRepository<Client, Long> {

    /**
     * Busca un cliente por su documento de identidad y su tienda.
     *
     * @param numero Número de documento (DNI/RUC).
     * @param storeId ID de la tienda.
     * @return Optional con el cliente.
     */
    Optional<Client> findByDocumentoIdentidadNumeroAndStoreId(String numero, Long storeId);

    /**
     * Lista todos los clientes pertenecientes a una tienda.
     *
     * @param storeId ID de la tienda.
     * @return Lista de clientes.
     */
    List<Client> findByStoreId(Long storeId);
}

