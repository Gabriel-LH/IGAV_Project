package com.igav.igav_project.Model.Entity.Payment;

import com.igav.igav_project.Model.Entity.Order.Order;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa una transacción de pago realizada dentro de un contrato u orden.
 * Vincula comprobantes de pago impresos o electrónicos y número de operación.
 * Cumple con los requerimientos RF-08, RF-09 y RF-10.
 *
 * @author IGAV Development Team
 */
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pago", nullable = false)
    private PaymentType tipoPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    private PaymentMethod metodoPago;

    @Column(name = "referencia_transaccion", length = 100)
    private String referenciaTransaccion;

    @Column(name = "comprobante_numero", length = 50)
    private String comprobanteNumero;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;

    @Embedded
    private AuditMetadata auditMetadata;

    /**
     * Constructor protegido para JPA.
     */
    protected Payment() {
    }

    /**
     * Constructor privado de inicialización.
     */
    private Payment(
            Order order,
            BigDecimal monto,
            PaymentType tipoPago,
            PaymentMethod metodoPago,
            String referenciaTransaccion,
            String comprobanteNumero,
            String createdBy
    ) {
        if (order == null) {
            throw new IllegalArgumentException("La orden es obligatoria.");
        }
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor a cero.");
        }

        this.order = order;
        this.monto = monto;
        this.tipoPago = tipoPago;
        this.metodoPago = metodoPago;
        this.referenciaTransaccion = referenciaTransaccion;
        this.comprobanteNumero = comprobanteNumero;
        this.fechaPago = LocalDateTime.now();
        this.auditMetadata = AuditMetadata.create(createdBy);
    }

    /**
     * Método estático de fábrica para crear transacciones de pago.
     *
     * @param order Contrato u orden asociada.
     * @param monto Monto abonado o reembolsado.
     * @param tipoPago Concepto (COBRO_ORDEN, DEPOSITO_GARANTIA, etc.).
     * @param metodoPago Medio utilizado (EFECTIVO, YAPE, etc.).
     * @param referenciaTransaccion Número de operación bancaria/billetera.
     * @param comprobanteNumero Número correlativo de comprobante emitido (RF-10).
     * @param createdBy Usuario en caja que registra el cobro.
     * @return Nueva instancia de {@link Payment}.
     */
    public static Payment create(
            Order order,
            BigDecimal monto,
            PaymentType tipoPago,
            PaymentMethod metodoPago,
            String referenciaTransaccion,
            String comprobanteNumero,
            String createdBy
    ) {
        return new Payment(order, monto, tipoPago, metodoPago, referenciaTransaccion, comprobanteNumero, createdBy);
    }

    // Getters
    public Long getId() { return id; }
    public Order getOrder() { return order; }
    public BigDecimal getMonto() { return monto; }
    public PaymentType getTipoPago() { return tipoPago; }
    public PaymentMethod getMetodoPago() { return metodoPago; }
    public String getReferenciaTransaccion() { return referenciaTransaccion; }
    public String getComprobanteNumero() { return comprobanteNumero; }
    public LocalDateTime getFechaPago() { return fechaPago; }
    public AuditMetadata getAuditMetadata() { return auditMetadata; }
}
