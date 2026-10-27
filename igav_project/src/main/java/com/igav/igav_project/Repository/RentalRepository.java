package com.igav.igav_project.Repository;

import com.igav.igav_project.Model.Entity.Commerce.Rental.Rental;
import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalStatus;
import com.igav.igav_project.Model.Entity.Commerce.Reservation.ReservationStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la administración de Contratos de Alquiler y Ventas (RF-04, RF-06).
 * Incluye la consulta de detección de solapamiento de fechas y colisiones en reservas.
 *
 * @author IGAV Development Team
 */
public interface RentalRepository extends JpaRepository<Rental, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT rental FROM Rental rental WHERE rental.id = :rentalId")
    Optional<Rental> findByIdForUpdate(@Param("rentalId") Long rentalId);

    /**
     * Obtiene los contratos de una tienda específica (Aislamiento por Tenant, RF-01).
     *
     * @param storeId ID de la tienda.
     * @return Lista de contratos.
     */
    List<Rental> findByBranchId(Long branchId);

    /**
    * Consulta para detectar solapamiento de alquileres de una prenda serializada.
     *
         * @param inventoryItemId ID de la unidad de inventario a validar.
         * @param fechaEntrega Inicio solicitado.
         * @param fechaDevolucion Fin solicitado.
         * @param estadosBloqueantes Estados que ocupan la prenda.
     * @return true si existe colisión de fechas.
     */
        @Query("SELECT COUNT(r) > 0 FROM Rental r JOIN r.rentalItems item " +
            "WHERE item.inventoryItem.id = :inventoryItemId " +
            "AND r.isDeleted = false " +
            "AND r.status IN :estadosBloqueantes " +
            "AND r.outDate < :fechaDevolucion " +
            "AND r.expectedReturnDate > :fechaEntrega")
        boolean existsOverlappingRental(
             @Param("inventoryItemId") Long inventoryItemId,
            @Param("fechaEntrega") LocalDateTime fechaEntrega,
            @Param("fechaDevolucion") LocalDateTime fechaDevolucion,
             @Param("estadosBloqueantes") List<RentalStatus> estadosBloqueantes
    );

            @Query("SELECT COUNT(reservation) > 0 FROM Reservation reservation " +
               "JOIN reservation.reservationItems item " +
               "WHERE item.inventoryItem.id = :inventoryItemId " +
               "AND reservation.id <> :reservationId " +
               "AND reservation.status = :status " +
               "AND reservation.startDate < :fechaDevolucion " +
               "AND reservation.endDate > :fechaEntrega")
            boolean existsOverlappingConfirmedReservation(
                @Param("inventoryItemId") Long inventoryItemId,
                @Param("reservationId") Long reservationId,
                @Param("fechaEntrega") Date fechaEntrega,
                @Param("fechaDevolucion") Date fechaDevolucion,
                @Param("status") ReservationStatus status
            );
}

