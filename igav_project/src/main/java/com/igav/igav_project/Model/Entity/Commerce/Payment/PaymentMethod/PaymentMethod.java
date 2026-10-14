package com.igav.igav_project.Model.Entity.Commerce.Payment.PaymentMethod;


import java.util.ArrayList;
import java.util.List;

import com.igav.igav_project.Model.Entity.Commerce.Payment.Payment;
import com.igav.igav_project.Model.Entity.Organization.Tenant.Tenant;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table (name = "payment_methods")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class PaymentMethod {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method_type", nullable = false)
    private PaymentMethodType paymentMethodType;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "allow_change_status", nullable = false)
    private boolean allowChangeStatus;

    @Column(name = "requires_pin", nullable = false)
    private boolean requiresPin;

    @Column(name = "icon_url")
    private String iconUrl;

    @OneToMany (mappedBy = "paymentMethod", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();


}
