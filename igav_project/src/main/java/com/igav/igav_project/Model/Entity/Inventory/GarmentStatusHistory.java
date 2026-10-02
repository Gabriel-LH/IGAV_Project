package com.igav.igav_project.Model.Entity.Inventory;

import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA de Auditoría Histórica de Transición de Estados de Prendas.
 * Permite trazabilidad completa de cambios de estado (DISPONIBLE -> ALQUILADO -> EN_TINTORERIA).
 */
@Entity
@Table(name = "garment_status_history")
public class GarmentStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garment_id", nullable = false)
    private Garment garment;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status")
    private GarmentStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false)
    private GarmentStatus toStatus;

    @Column(name = "reason", length = 300)
    private String reason;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    @Embedded
    private AuditMetadata auditMetadata;

    protected GarmentStatusHistory() {
    }

    private GarmentStatusHistory(Garment garment, GarmentStatus fromStatus, GarmentStatus toStatus, String reason, String updatedBy) {
        this.garment = garment;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.reason = reason;
        this.changedAt = LocalDateTime.now();
        this.auditMetadata = AuditMetadata.create(updatedBy);
    }

    public static GarmentStatusHistory create(Garment garment, GarmentStatus fromStatus, GarmentStatus toStatus, String reason, String updatedBy) {
        return new GarmentStatusHistory(garment, fromStatus, toStatus, reason, updatedBy);
    }

    public Long getId() { return id; }
    public Garment getGarment() { return garment; }
    public GarmentStatus getFromStatus() { return fromStatus; }
    public GarmentStatus getToStatus() { return toStatus; }
    public String getReason() { return reason; }
    public LocalDateTime getChangedAt() { return changedAt; }
    public AuditMetadata getAuditMetadata() { return auditMetadata; }
}
