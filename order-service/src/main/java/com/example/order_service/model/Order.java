package com.example.order_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_generator")
    @SequenceGenerator(name = "order_id_generator", sequenceName = "order_id_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "order_number", nullable = false)
    private String orderNumber;

    @Column(nullable = false)
    private String username;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "order",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<OrderItem> orderItems;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "customer_name")),
            @AttributeOverride(name = "email", column = @Column(name = "customer_email")),
            @AttributeOverride(name = "phone", column = @Column(name = "customer_phone"))
    }
    )
    private Customer customer;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "addressLine1", column = @Column(name = "delivery_address_line1")),
            @AttributeOverride(name = "addressLine2", column = @Column(name = "delivery_address_line2")),
            @AttributeOverride(name = "city", column = @Column(name = "delivery_address_city")),
            @AttributeOverride(name = "state", column = @Column(name = "delivery_address_state")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "delivery_address_zipcode")),
            @AttributeOverride(name = "country", column = @Column(name = "delivery_address_country")),
    }
    )
    private Address deliveryAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(length = 500)
    private String comments;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    private void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
