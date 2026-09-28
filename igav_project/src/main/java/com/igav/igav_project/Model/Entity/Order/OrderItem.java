package com.igav.igav_project.Model.Entity.Order;

import com.igav.igav_project.Model.Entity.Inventory.Garment;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entidad JPA que representa el detalle individual de una prenda incluida en una orden/contrato.
 * Registra los precios y depósitos vigentes al momento de la firma de la transacción.
 *
 * @author IGAV Development Team
 */
@Entity
@Table(name = "order_items")
public class OrderItem {

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
    @Column(name = "tipo_item", nullable = false)
    private OrderItemType tipoItem;

    @Column(name = "precio_aplicado", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioAplicado;

    @Column(name = "garantia_aplicada", nullable = false, precision = 10, scale = 2)
    private BigDecimal garantiaAplicada;

    @Column(name = "observaciones_devolucion", length = 255)
    private String observacionesDevolucion;

    /**
     * Constructor protegido para JPA.
     */
    protected OrderItem() {
    }

    /**
     * Constructor privado de inicialización.
     */
    private OrderItem(Order order, Garment garment, OrderItemType tipoItem, BigDecimal precioAplicado, BigDecimal garantiaAplicada) {
        if (garment == null) {
            throw new IllegalArgumentException("La prenda es obligatoria en el detalle de la orden.");
        }
        if (precioAplicado == null || precioAplicado.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio aplicado no puede ser nulo o negativo.");
        }
        this.order = order;
        this.garment = garment;
        this.tipoItem = tipoItem;
        this.precioAplicado = precioAplicado;
        this.garantiaAplicada = garantiaAplicada != null ? garantiaAplicada : BigDecimal.ZERO;
    }

    /**
     * Método estático para crear un ítem de orden.
     *
     * @param order Contrato u orden contenedora.
     * @param garment Prenda seleccionada.
     * @param tipoItem Alquiler o Venta.
     * @param precioAplicado Monto acordado del servicio o venta.
     * @param garantiaAplicada Depósito de garantía asignado.
     * @return Instancia de {@link OrderItem}.
     */
    public static OrderItem create(Order order, Garment garment, OrderItemType tipoItem, BigDecimal precioAplicado, BigDecimal garantiaAplicada) {
        return new OrderItem(order, garment, tipoItem, precioAplicado, garantiaAplicada);
    }

    // Setters y Getters
    public void setOrder(Order order) { this.order = order; }
    public void setObservacionesDevolucion(String observaciones) { this.observacionesDevolucion = observaciones; }

    public Long getId() { return id; }
    public Order getOrder() { return order; }
    public Garment getGarment() { return garment; }
    public OrderItemType getTipoItem() { return tipoItem; }
    public BigDecimal getPrecioAplicado() { return precioAplicado; }
    public BigDecimal getGarantiaAplicada() { return garantiaAplicada; }
    public String getObservacionesDevolucion() { return observacionesDevolucion; }
}
