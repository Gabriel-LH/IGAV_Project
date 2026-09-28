package com.igav.igav_project.Model.Entity.Maintenance;

import com.igav.igav_project.Model.Entity.Inventory.Garment;
import com.igav.igav_project.Model.Entity.Order.Order;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entidad JPA que registra las incidencias de daños o retrasos detectadas en la devolución de prendas.
 * Permite calcular el descuento y retención de depósitos de garantía.
 * Cumple con los requerimientos RF-08 y RF-13.
 *
 * @author IGAV Development Team
 */
@Entity
@Table(name = "return_incidents")
public class ReturnIncident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garment_id", nullable = false)
    private Garment garment;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_incidencia", nullable = false)
    private IncidentType tipoIncidencia;

    @Column(name = "monto_descuento_garantia", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoDescuentoGarantia;

    @Column(name = "descripcion", nullable = false, length = 500)
    private String descripcion;

    @Embedded
    private AuditMetadata auditMetadata;

    /**
     * Constructor protegido para JPA.
     */
    protected ReturnIncident() {
    }

    /**
     * Constructor privado de inicialización.
     */
    private ReturnIncident(
            Order order,
            Garment garment,
            IncidentType tipoIncidencia,
            BigDecimal montoDescuentoGarantia,
            String descripcion,
            String createdBy
    ) {
        if (order == null) {
            throw new IllegalArgumentException("La orden es obligatoria.");
        }
        if (garment == null) {
            throw new IllegalArgumentException("La prenda es obligatoria.");
        }
        if (montoDescuentoGarantia == null || montoDescuentoGarantia.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto a descontar de la garantía debe ser no negativo.");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción de la incidencia es obligatoria.");
        }

        this.order = order;
        this.garment = garment;
        this.tipoIncidencia = tipoIncidencia;
        this.montoDescuentoGarantia = montoDescuentoGarantia;
        this.descripcion = descripcion;
        this.auditMetadata = AuditMetadata.create(createdBy);
    }

    /**
     * Método estático de fábrica para crear incidencias de devolución.
     *
     * @param order Contrato u orden en devolución.
     * @param garment Prenda afectada.
     * @param tipoIncidencia Tipo de falla (MANCHA, ROTURA, RETRASO, etc.).
     * @param montoDescuentoGarantia Monto deducido del depósito de garantía.
     * @param descripcion Detalle de la avería observada.
     * @param createdBy Usuario receptor que registra la incidencia.
     * @return Nueva instancia de {@link ReturnIncident}.
     */
    public static ReturnIncident create(
            Order order,
            Garment garment,
            IncidentType tipoIncidencia,
            BigDecimal montoDescuentoGarantia,
            String descripcion,
            String createdBy
    ) {
        return new ReturnIncident(order, garment, tipoIncidencia, montoDescuentoGarantia, descripcion, createdBy);
    }

    // Getters
    public Long getId() { return id; }
    public Order getOrder() { return order; }
    public Garment getGarment() { return garment; }
    public IncidentType getTipoIncidencia() { return tipoIncidencia; }
    public BigDecimal getMontoDescuentoGarantia() { return montoDescuentoGarantia; }
    public String getDescripcion() { return descripcion; }
    public AuditMetadata getAuditMetadata() { return auditMetadata; }
}
