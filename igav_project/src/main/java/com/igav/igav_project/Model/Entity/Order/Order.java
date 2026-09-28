package com.igav.igav_project.Model.Entity.Order;

import com.igav.igav_project.Model.Entity.Store.Store;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad principal JPA que representa un Contrato de Alquiler u Orden de Venta Directa.
 * Controla fechas reservadas, montos de alquiler, depósitos de garantía y estado de liquidación.
 * Cumple con los requerimientos RF-04, RF-06, RF-07, RF-08, RF-11 y RF-13.
 *
 * @author IGAV Development Team
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_contrato", nullable = false, unique = true, length = 50)
    private String codigoContrato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_orden", nullable = false)
    private OrderType tipo;

    @Column(name = "fecha_orden", nullable = false)
    private LocalDateTime fechaOrden;

    @Column(name = "fecha_entrega_acordada", nullable = false)
    private LocalDateTime fechaEntregaAcordada;

    @Column(name = "fecha_devolucion_acordada")
    private LocalDateTime fechaDevolucionAcordada;

    @Column(name = "fecha_devolucion_real")
    private LocalDateTime fechaDevolucionReal;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "monto_garantia_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoGarantiaTotal;

    @Column(name = "descuento_garantia", nullable = false, precision = 10, scale = 2)
    private BigDecimal descuentoGarantia;

    @Column(name = "monto_penalizacion", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoPenalizacion;

    @Column(name = "monto_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private OrderStatus estado;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Embedded
    private AuditMetadata auditMetadata;

    /**
     * Constructor protegido para JPA.
     */
    protected Order() {
    }

    /**
     * Constructor privado de inicialización.
     */
    private Order(
            String codigoContrato,
            Customer customer,
            Store store,
            OrderType tipo,
            LocalDateTime fechaEntregaAcordada,
            LocalDateTime fechaDevolucionAcordada,
            String observaciones,
            String createdBy
    ) {
        if (codigoContrato == null || codigoContrato.isBlank()) {
            throw new IllegalArgumentException("El código de contrato es obligatorio.");
        }
        if (customer == null) {
            throw new IllegalArgumentException("El cliente es obligatorio.");
        }
        if (store == null) {
            throw new IllegalArgumentException("La tienda es obligatoria.");
        }
        if (fechaEntregaAcordada == null) {
            throw new IllegalArgumentException("La fecha acordada de entrega es obligatoria.");
        }

        this.codigoContrato = codigoContrato.trim().toUpperCase();
        this.customer = customer;
        this.store = store;
        this.tipo = tipo;
        this.fechaOrden = LocalDateTime.now();
        this.fechaEntregaAcordada = fechaEntregaAcordada;
        this.fechaDevolucionAcordada = fechaDevolucionAcordada;
        this.subtotal = BigDecimal.ZERO;
        this.montoGarantiaTotal = BigDecimal.ZERO;
        this.descuentoGarantia = BigDecimal.ZERO;
        this.montoPenalizacion = BigDecimal.ZERO;
        this.montoTotal = BigDecimal.ZERO;
        this.estado = OrderStatus.BORRADOR;
        this.observaciones = observaciones;
        this.auditMetadata = AuditMetadata.create(createdBy);
    }

    /**
     * Método de fábrica estático para instanciar contratos u órdenes.
     *
     * @param codigoContrato Código correlativo único (ej. ORD-2026-001).
     * @param customer Cliente comprador o arrendatario.
     * @param store Tienda emisora.
     * @param tipo Tipo de orden (ALQUILER, VENTA, MIXTO).
     * @param fechaEntregaAcordada Fecha en la que la prenda será entregada.
     * @param fechaDevolucionAcordada Fecha máxima de devolución (en alquiler).
     * @param observaciones Notas aclaratorias del contrato.
     * @param createdBy Usuario responsable.
     * @return Nueva instancia de {@link Order}.
     */
    public static Order create(
            String codigoContrato,
            Customer customer,
            Store store,
            OrderType tipo,
            LocalDateTime fechaEntregaAcordada,
            LocalDateTime fechaDevolucionAcordada,
            String observaciones,
            String createdBy
    ) {
        return new Order(codigoContrato, customer, store, tipo, fechaEntregaAcordada, fechaDevolucionAcordada, observaciones, createdBy);
    }

    /**
     * Agrega una prenda al contrato y recalcula los totales automáticamente.
     *
     * @param item Instancia de {@link OrderItem}.
     */
    public void addItem(OrderItem item) {
        item.setOrder(this);
        this.items.add(item);
        recalcularTotales();
    }

    /**
     * Recalcula el subtotal, total de garantía y monto total de la orden.
     */
    public void recalcularTotales() {
        this.subtotal = this.items.stream()
                .map(OrderItem::getPrecioAplicado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.montoGarantiaTotal = this.items.stream()
                .map(OrderItem::getGarantiaAplicada)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Monto total = Subtotal (servicios/ventas) + Penalizaciones - Descuentos
        this.montoTotal = this.subtotal
                .add(this.montoPenalizacion);
    }

    /**
     * Confirma la orden bloqueando las fechas reservadas en catálogo.
     *
     * @param updatedBy Usuario responsable.
     */
    public void confirmarOrden(String updatedBy) {
        this.estado = OrderStatus.CONFIRMADA;
        this.auditMetadata = this.auditMetadata.update(updatedBy);
    }

    /**
     * Cambia el estado a EN_ALQUILER cuando el cliente retira las prendas.
     *
     * @param updatedBy Usuario que efectúa el despacho.
     */
    public void despacharAlquiler(String updatedBy) {
        this.estado = OrderStatus.EN_ALQUILER;
        this.auditMetadata = this.auditMetadata.update(updatedBy);
    }

    /**
     * Registra la recepción física de devoluciones (RF-13).
     *
     * @param fechaReal Marca de tiempo de recepción.
     * @param descuentoGarantia Monto a retener de la garantía por daños.
     * @param montoPenalizacion Penalizaciones aplicables por mora.
     * @param updatedBy Usuario receptor.
     */
    public void registrarDevolucion(LocalDateTime fechaReal, BigDecimal descuentoGarantia, BigDecimal montoPenalizacion, String updatedBy) {
        this.fechaDevolucionReal = fechaReal;
        this.descuentoGarantia = descuentoGarantia != null ? descuentoGarantia : BigDecimal.ZERO;
        this.montoPenalizacion = montoPenalizacion != null ? montoPenalizacion : BigDecimal.ZERO;
        this.estado = OrderStatus.DEVUELTO_PENDIENTE_TINTORERIA;
        recalcularTotales();
        this.auditMetadata = this.auditMetadata.update(updatedBy);
    }

    /**
     * Cierra el contrato de alquiler/venta definitivamente tras liquidar pagos.
     *
     * @param updatedBy Usuario que realiza el cierre.
     */
    public void completarOrden(String updatedBy) {
        this.estado = OrderStatus.COMPLETADA;
        this.auditMetadata = this.auditMetadata.update(updatedBy);
    }

    // Getters
    public Long getId() { return id; }
    public String getCodigoContrato() { return codigoContrato; }
    public Customer getCustomer() { return customer; }
    public Store getStore() { return store; }
    public OrderType getTipo() { return tipo; }
    public LocalDateTime getFechaOrden() { return fechaOrden; }
    public LocalDateTime getFechaEntregaAcordada() { return fechaEntregaAcordada; }
    public LocalDateTime getFechaDevolucionAcordada() { return fechaDevolucionAcordada; }
    public LocalDateTime getFechaDevolucionReal() { return fechaDevolucionReal; }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getMontoGarantiaTotal() { return montoGarantiaTotal; }
    public BigDecimal getDescuentoGarantia() { return descuentoGarantia; }
    public BigDecimal getMontoPenalizacion() { return montoPenalizacion; }
    public BigDecimal getMontoTotal() { return montoTotal; }
    public OrderStatus getEstado() { return estado; }
    public String getObservaciones() { return observaciones; }
    public List<OrderItem> getItems() { return items; }
    public AuditMetadata getAuditMetadata() { return auditMetadata; }
}
