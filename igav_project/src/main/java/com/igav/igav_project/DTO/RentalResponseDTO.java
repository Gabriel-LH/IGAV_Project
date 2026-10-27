package com.igav.igav_project.DTO;

import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Respuesta de un alquiler y de su liquidación.
 *
 * @author IGAV Development Team
 */
public record RentalResponseDTO(
    Long id,
    String codigoAlquiler,
    Long clientId,
    String clienteNombreCompleto,
    Long branchId,
    String tipo,
    LocalDateTime fechaSalida,
    LocalDateTime fechaDevolucionPrevista,
    LocalDateTime fechaDevolucionReal,
    BigDecimal subtotal,
    BigDecimal descuento,
    BigDecimal garantiaTotal,
    BigDecimal garantiaAplicada,
    BigDecimal mora,
    BigDecimal totalCargos,
    BigDecimal garantiaDevuelta,
    RentalStatus status,
    List<RentalItemResponseDTO> items
) {}