package com.igav.igav_project.Repository;

import com.igav.igav_project.Model.Entity.Order.Order;
import com.igav.igav_project.Model.Entity.Order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la administración de Contratos de Alquiler y Ventas (RF-04, RF-06).
 * Incluye la consulta de detección de solapamiento de fechas y colisiones en reservas.
 *
 * @author IGAV Development Team
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Busca un contrato por su código único correlativo.
     *
     * @param codigoContrato Código correlativo.
     * @return Optional con la orden.
     */
    Optional<Order> findByCodigoContrato(String codigoContrato);

    /**
     * Obtiene los contratos de una tienda específica (Aislamiento por Tenant, RF-01).
     *
     * @param storeId ID de la tienda.
     * @return Lista de contratos.
     */
    List<Order> findByStoreId(Long storeId);

    /**
     * Consulta para detectar solapamiento o cruce de fechas en la reserva de una prenda específica (RF-04).
     * Retorna true si existe al menos una orden activa o confirmada para la prenda en el rango solicitado.
     *
     * @param garmentId ID de la prenda a validar.
     * @param fechaEntrega Fecha estimada de retiro/entrega.
     * @param fechaDevolucion Fecha estimada de retorno.
     * @param estadosIgnorados Estados cancelados o borrador que no bloquean inventario.
     * @return true si existe colisión de fechas.
     */
    @Query("SELECT COUNT(o) > 0 FROM Order o JOIN o.items item " +
           "WHERE item.garment.id = :garmentId " +
           "AND o.estado NOT IN :estadosIgnorados " +
           "AND (o.fechaEntregaAcordada < :fechaDevolucion AND o.fechaDevolucionAcordada > :fechaEntrega)")
    boolean existsOverlappingReservation(
            @Param("garmentId") Long garmentId,
            @Param("fechaEntrega") LocalDateTime fechaEntrega,
            @Param("fechaDevolucion") LocalDateTime fechaDevolucion,
            @Param("estadosIgnorados") List<OrderStatus> estadosIgnorados
    );
}

