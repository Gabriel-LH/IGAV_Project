package com.igav.igav_project.Model.Entity.Inventory;

import com.igav.igav_project.Model.Entity.Store.Store;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entidad JPA que representa una Prenda o artículo de vestuario dentro del inventario de la tienda.
 * Registra código único (SKU), características, costos de alquiler/venta, garantía y ciclo de vida.
 * Cumple con los requerimientos RF-03, RF-04, RF-05, RF-11 y RF-12.
 *
 * @author IGAV Development Team
 */
@Entity
@Table(name = "garments")
public class Garment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_unico", nullable = false, unique = true, length = 50)
    private String codigoUnico;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "color", length = 50)
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(name = "talla", nullable = false)
    private GarmentSize talla;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "precio_alquiler", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioAlquiler;

    @Column(name = "precio_venta", precision = 10, scale = 2)
    private BigDecimal precioVenta;

    @Column(name = "deposito_garantia", nullable = false, precision = 10, scale = 2)
    private BigDecimal depositoGarantia;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private GarmentStatus estado;

    @Column(name = "usos_acumulados", nullable = false)
    private int usosAcumulados;

    @Column(name = "max_usos_recomendados", nullable = false)
    private int maxUsosRecomendados;

    @Column(name = "horas_tintoreria_bloqueo", nullable = false)
    private int horasTintoreriaBloqueo;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @Embedded
    private AuditMetadata auditMetadata;

    /**
     * Constructor protegido para JPA.
     */
    protected Garment() {
    }

    /**
     * Constructor privado para forzar la creación mediante el método de fábrica estático.
     */
    private Garment(
            String codigoUnico,
            String nombre,
            String descripcion,
            String color,
            GarmentSize talla,
            Category category,
            Store store,
            BigDecimal precioAlquiler,
            BigDecimal precioVenta,
            BigDecimal depositoGarantia,
            int maxUsosRecomendados,
            int horasTintoreriaBloqueo,
            String createdBy
    ) {
        if (codigoUnico == null || codigoUnico.isBlank()) {
            throw new IllegalArgumentException("El código único de la prenda es obligatorio.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la prenda es obligatorio.");
        }
        if (precioAlquiler == null || precioAlquiler.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio de alquiler debe ser un valor no negativo.");
        }
        if (depositoGarantia == null || depositoGarantia.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El depósito de garantía debe ser un valor no negativo.");
        }

        this.codigoUnico = codigoUnico.trim().toUpperCase();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.color = color;
        this.talla = talla;
        this.category = category;
        this.store = store;
        this.precioAlquiler = precioAlquiler;
        this.precioVenta = precioVenta;
        this.depositoGarantia = depositoGarantia;
        this.estado = GarmentStatus.DISPONIBLE;
        this.usosAcumulados = 0;
        this.maxUsosRecomendados = maxUsosRecomendados > 0 ? maxUsosRecomendados : 15;
        this.horasTintoreriaBloqueo = horasTintoreriaBloqueo > 0 ? horasTintoreriaBloqueo : 24;
        this.auditMetadata = AuditMetadata.create(createdBy);
    }

    /**
     * Método de fábrica estático para instanciar prendas.
     *
     * @param codigoUnico Código SKU único.
     * @param nombre Nombre descriptivo de la prenda.
     * @param descripcion Detalles adicionales de la prenda.
     * @param color Color de la prenda.
     * @param talla Talla según {@link GarmentSize}.
     * @param category Categoría asignada.
     * @param store Tienda propietaria de la prenda.
     * @param precioAlquiler Tarifa por periodo de alquiler.
     * @param precioVenta Precio opcional en caso de venta directa.
     * @param depositoGarantia Garantía requerida.
     * @param maxUsosRecomendados Límite recomendado de usos antes de alerta de baja.
     * @param horasTintoreriaBloqueo Horas necesarias de bloqueo en lavandería/tintorería.
     * @param createdBy Usuario que crea el registro.
     * @return Instancia de {@link Garment}.
     */
    public static Garment create(
            String codigoUnico,
            String nombre,
            String descripcion,
            String color,
            GarmentSize talla,
            Category category,
            Store store,
            BigDecimal precioAlquiler,
            BigDecimal precioVenta,
            BigDecimal depositoGarantia,
            int maxUsosRecomendados,
            int horasTintoreriaBloqueo,
            String createdBy
    ) {
        return new Garment(
            codigoUnico, nombre, descripcion, color, talla, category, store,
            precioAlquiler, precioVenta, depositoGarantia, maxUsosRecomendados, horasTintoreriaBloqueo, createdBy
        );
    }

    /**
     * Incrementa el historial de usos/alquileres acumulados (RF-12).
     *
     * @param updatedBy Usuario que registra la acción.
     */
    public void registrarUso(String updatedBy) {
        this.usosAcumulados++;
        this.auditMetadata = this.auditMetadata.update(updatedBy);
    }

    /**
     * Cambia el estado a EN_TINTORERIA para bloqueo preventivo pos-alquiler (RF-05).
     *
     * @param updatedBy Usuario que modifica el estado.
     */
    public void enviarATintoreria(String updatedBy) {
        this.estado = GarmentStatus.EN_TINTORERIA;
        this.auditMetadata = this.auditMetadata.update(updatedBy);
    }

    /**
     * Marca la prenda como DISPONIBLE tras la culminación del mantenimiento.
     *
     * @param updatedBy Usuario que libera la prenda.
     */
    public void marcarDisponible(String updatedBy) {
        this.estado = GarmentStatus.DISPONIBLE;
        this.auditMetadata = this.auditMetadata.update(updatedBy);
    }

    /**
     * Marca la prenda como ALQUILADO al ser despachada al cliente.
     *
     * @param updatedBy Usuario que realiza el despacho.
     */
    public void marcarAlquilado(String updatedBy) {
        this.estado = GarmentStatus.ALQUILADO;
        this.auditMetadata = this.auditMetadata.update(updatedBy);
    }

    /**
     * Verifica si la prenda ha superado su límite de rotación sugerido (RF-12).
     *
     * @return true si los usos superan el máximo recomendado.
     */
    public boolean requiereAlertaRotacion() {
        return this.usosAcumulados >= this.maxUsosRecomendados;
    }

    // Getters
    public Long getId() { return id; }
    public String getCodigoUnico() { return codigoUnico; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getColor() { return color; }
    public GarmentSize getTalla() { return talla; }
    public Category getCategory() { return category; }
    public Store getStore() { return store; }
    public BigDecimal getPrecioAlquiler() { return precioAlquiler; }
    public BigDecimal getPrecioVenta() { return precioVenta; }
    public BigDecimal getDepositoGarantia() { return depositoGarantia; }
    public GarmentStatus getEstado() { return estado; }
    public int getUsosAcumulados() { return usosAcumulados; }
    public int getMaxUsosRecomendados() { return maxUsosRecomendados; }
    public int getHorasTintoreriaBloqueo() { return horasTintoreriaBloqueo; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public AuditMetadata getAuditMetadata() { return auditMetadata; }
}
