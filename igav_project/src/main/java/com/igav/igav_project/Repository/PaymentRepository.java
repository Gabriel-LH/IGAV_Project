package com.igav.igav_project.Repository;

import com.igav.igav_project.Model.Entity.Payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio JPA para el registro de movimientos financieros y comprobantes (RF-08, RF-09, RF-10).
 *
 * @author IGAV Development Team
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Obtiene todos los pagos realizados dentro de una orden o contrato.
     *
     * @param orderId ID de la orden.
     * @return Lista de pagos.
     */
    List<Payment> findByOrderId(Long orderId);
}

