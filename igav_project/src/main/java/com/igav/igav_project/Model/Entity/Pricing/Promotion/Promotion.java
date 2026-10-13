package com.igav.igav_project.Model.Entity.Pricing.Promotion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.igav.igav_project.Model.Entity.Commerce.Reservation.ReservationItem;
import com.igav.igav_project.Model.Shared.ValueObjects.AuditMetadata;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table (name = "promtions")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class Promotion {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id; 

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PromotionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "scope", nullable = false)
    private PromotionScope scope;

    @ElementCollection
    @CollectionTable(name = "promotion_target_ids", joinColumns = @JoinColumn(name = "promotion_id"))
    @Column(name = "target_id")
    @Builder.Default
    private List<String> targetIds = new ArrayList<>();

    @Column(name = "value", precision = 12, scale = 2)
    private BigDecimal value;

    @ElementCollection
    @CollectionTable(name = "promotion_applies_to_types", joinColumns = @JoinColumn(name = "promotion_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "applies_to_type")
    @Builder.Default
    private List<AppliesToType> appliesToType = new ArrayList<>();

    @Embedded
    private BundleConfigEmbeddable bundleConfig;

    @Column(name = "is_exclusive")
    private boolean isExclusive;

    @Column(name = "code")
    private String code;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "min_purchase_amount", precision = 12, scale = 2)
    private BigDecimal minPurchaseAmount;

    @Column(name = "max_uses")
    private Integer maxUses;

    @Column(name = "used_count")
    private Integer usedCount;

    @Column(name = "requires_code")
    private boolean requiresCode;

    @Column(name = "single_user_per_customer")
    private boolean singleUserPerCustomer;

    @Enumerated(EnumType.STRING)
    @Column(name = "usage_type", nullable = false)
    private UsageType usageType;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Embedded 
    private AuditMetadata auditMetadata;

    @OneToMany (mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationItem> reservationItems;


}
