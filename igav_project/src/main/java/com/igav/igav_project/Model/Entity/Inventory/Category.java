package com.igav.igav_project.Model.Entity.Inventory;

import com.igav.igav_project.Model.Entity.Store.Store;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;

/**
 * Entidad JPA que representa una Categoría de prendas dentro del catálogo de la tienda.
 * Ejemplos: Vestidos de Gala, Ternós de Novio, Sacos, Accesorios.
 * Cumple con el requerimiento RF-03.
 *
 * @author IGAV Development Team
 */
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Embedded
    private AuditMetadata auditMetadata;

    /**
     * Constructor protegido para JPA/Hibernate.
     */
    protected Category() {
    }

    /**
     * Constructor privado para forzar el uso del método de fábrica.
     */
    private Category(String nombre, String descripcion, Store store, String createdBy) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio.");
        }
        if (store == null) {
            throw new IllegalArgumentException("La tienda es obligatoria.");
        }
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.store = store;
        this.auditMetadata = AuditMetadata.create(createdBy);
    }

    /**
     * Método de fábrica para la creación de categorías.
     *
     * @param nombre Nombre de la categoría.
     * @param descripcion Descripción opcional de la categoría.
     * @param store Tienda a la que pertenece la categoría.
     * @param createdBy Usuario o sistema creador.
     * @return Nueva instancia de {@link Category}.
     */
    public static Category create(String nombre, String descripcion, Store store, String createdBy) {
        return new Category(nombre, descripcion, store, createdBy);
    }

    // Getters y Setters de negocio
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Store getStore() { return store; }
    public AuditMetadata getAuditMetadata() { return auditMetadata; }
}
