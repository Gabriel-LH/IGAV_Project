package com.igav.igav_project.Model.Entity.Maintenance;

import com.igav.igav_project.Model.Entity.Inventory.Garment;
import com.igav.igav_project.Model.Entity.Order.Order;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad JPA que registra el historial de lavados, tintorería y mantenimientos de cada prenda.
 * Permite trazabilidad del ciclo de vida y bloqueo automático de disponibilidad.
 * Cumple con los requerimientos RF-05 y RF-12.
 *
 * @author IGAV Development Team
 */
@Entity
@Table(name = "maintenance_records")
public class MaintenanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garment_id", nullable = false)
    private Garment garment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private MaintenanceType tipo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin_estimada", nullable = false)
    private LocalDateTime fechaFinEstimada;

    @Column(name = "fecha_fin_real")
    private LocalDateTime fechaFinReal;

    @Column(name = "costo", precision = 10, scale = 2)
    private BigDecimal costo;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "realizado_por", length = 100)
    private String realizadoPor;

    @Embedded
    private AuditMetadata auditMetadata;

    /**
     * Constructor protegido para JPA.
     */
    protected MaintenanceRecord() {
    }

    /**
     * Constructor privado de inicialización.
     */
    private MaintenanceRecord(
            Garment garment,
            Order order,
            MaintenanceType tipo,
            LocalDateTime fechaFinEstimada,
            BigDecimal costo,
            String observaciones,
            String realizadoPor,
            String createdBy
    ) {
        if (garment == null) {
            throw new IllegalArgumentException("La prenda es obligatoria en el registro de mantenimiento.");
        }
        if (fechaFinEstimada == null) {
            throw new IllegalArgumentException("La fecha estimada de fin es obligatoria.");
        }

        this.garment = garment;
        this.order = order;
        this.tipo = tipo;
        this.fechaInicio = LocalDateTime.now();
        this.fechaFinEstimada = fechaFinEstimada;
        this.costo = costo != null ? costo : BigDecimal.ZERO;
        this.observaciones = observaciones;
        this.realizadoPor = realizadoPor;
        this.auditMetadata = AuditMetadata.create(createdBy);
    }

    /**
     * Método estático para registrar un proceso de mantenimiento/tintorería.
     *
     * @param garment Prenda ingresada al mantenimiento.
     * @param order Orden o contrato de procedencia (opcional).
     * @param tipo Tipo de proceso (RF-05).
     * @param fechaFinEstimada Fecha en la que la prenda volverá a estar disponible.
     * @param costo Costo del servicio de tintorería/reparación.
     * @param observaciones Notas sobre el estado de la prenda.
     * @param realizadoPor Proveedor de tintorería o encargado de almacén.
     * @param createdBy Usuario responsable del registro.
     * @return Nueva instancia de {@link MaintenanceRecord}.
     */
    public static MaintenanceRecord create(
            Garment garment,
            Order order,
            MaintenanceType tipo,
            LocalDateTime fechaFinEstimada,
            BigDecimal costo,
            String observaciones,
            String realizadoPor,
            String createdBy
    ) {
        return new MaintenanceRecord(garment, order, tipo, fechaFinEstimada, costo, observaciones, realizadoPor, createdBy);
    }

    /**
     * Finaliza el mantenimiento y marca la prenda como disponible nuevamente.
     *
     * @param updatedBy Usuario que finaliza la tarea.
     */
    public void finalizarMantenimiento(String updatedBy) {
        this.fechaFinReal = LocalDateTime.now();
        this.garment.marcarDisponible(updatedBy);
        this.auditMetadata = this.auditMetadata.update(updatedBy);
    }

    // Getters
    public Long getId() { return id; }
    public Garment getGarment() { return garment; }
    public Order getOrder() { return order; }
    public MaintenanceType getTipo() { return tipo; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public LocalDateTime getFechaFinEstimada() { return fechaFinEstimada; }
    public LocalDateTime getFechaFinReal() { return fechaFinReal; }
    public BigDecimal getCosto() { return costo; }
    public String getObservaciones() { return observaciones; }
    public String getRealizadoPor() { return realizadoPor; }
    public AuditMetadata getAuditMetadata() { return auditMetadata; }
}
