package com.jeremiasAvero.app.order.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class OrderEntity {
    @Id @GeneratedValue
    private UUID id;

    /** Útil para mostrar al usuario (ej: "ORD-2025-000123") */
    @Column(nullable = false, length = 40)
    private String orderNumber;

    /** Null para guest checkout */
    private Long userId;


    /** Opcional, para trazabilidad desde el carrito */
    private UUID cartId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private OrderStatus status = OrderStatus.NEW;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private FulfillmentStatus fulfillmentStatus = FulfillmentStatus.UNFULFILLED;

    @Column(nullable = false, length = 3)
    private String currency = "ARS";

    @Column(precision = 19, scale = 2) private BigDecimal subtotal;
    @Column(precision = 19, scale = 2) private BigDecimal discount;
    @Column(precision = 19, scale = 2) private BigDecimal tax;
    @Column(precision = 19, scale = 2) private BigDecimal shipping;
    @Column(precision = 19, scale = 2) private BigDecimal total;

    /** Datos de contacto/buyer (para guest o confirmación) */
    @Column(length = 120) private String buyerEmail;
    @Column(length = 120) private String buyerName;
    @Column(length = 40)  private String buyerPhone;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="street",  column=@Column(name="ship_street")),
            @AttributeOverride(name="city",    column=@Column(name="ship_city")),
            @AttributeOverride(name="state",   column=@Column(name="ship_state")),
            @AttributeOverride(name="zip",     column=@Column(name="ship_zip")),
            @AttributeOverride(name="country", column=@Column(name="ship_country"))
    })
    private Address shippingAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="street",  column=@Column(name="bill_street")),
            @AttributeOverride(name="city",    column=@Column(name="bill_city")),
            @AttributeOverride(name="state",   column=@Column(name="bill_state")),
            @AttributeOverride(name="zip",     column=@Column(name="bill_zip")),
            @AttributeOverride(name="country", column=@Column(name="bill_country"))
    })
    private Address billingAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    @Version
    private long version;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    // ---------- helpers ----------
    public void addItem(OrderItem item) {
        item.setOrder(this);
        items.add(item);
    }

    @PrePersist
    void prePersist() {
        var now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }
    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }
}
