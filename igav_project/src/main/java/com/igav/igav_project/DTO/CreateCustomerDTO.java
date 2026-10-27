package com.igav.igav_project.DTO;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * DTO para el registro y alta de clientes en la base de datos MySQL (RF-06).
 *
 * @author IGAV Development Team
 */
public record CreateCustomerDTO(
    String nombres,
    String apellidos,
    String tipoDocumento,
    String numeroDocumento,
    String email,
    String telefono,
    String direccion,
    @JsonAlias("storeId") Long branchId
) {}
