package com.igav.igav_project.Repository;

import com.igav.igav_project.Model.Entity.Commerce.Rental.RentalReturnInspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RentalReturnInspectionRepository extends JpaRepository<RentalReturnInspection, Long> {

    @Query("SELECT inspection FROM RentalReturnInspection inspection " +
           "WHERE inspection.rentalItem.rental.id = :rentalId ORDER BY inspection.id")
    List<RentalReturnInspection> findByRentalId(@Param("rentalId") Long rentalId);
}