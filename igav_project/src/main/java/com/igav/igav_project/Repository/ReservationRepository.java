package com.igav.igav_project.Repository;

import com.igav.igav_project.Model.Entity.Commerce.Reservation.Reservation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT reservation FROM Reservation reservation WHERE reservation.id = :reservationId")
	Optional<Reservation> findByIdForUpdate(@Param("reservationId") Long reservationId);
}