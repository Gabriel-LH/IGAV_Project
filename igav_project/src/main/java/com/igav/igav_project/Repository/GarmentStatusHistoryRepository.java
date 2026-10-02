package com.igav.igav_project.Repository;

import com.igav.igav_project.Model.Entity.Inventory.GarmentStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GarmentStatusHistoryRepository extends JpaRepository<GarmentStatusHistory, Long> {
    List<GarmentStatusHistory> findByGarmentIdOrderByChangedAtDesc(Long garmentId);
}

