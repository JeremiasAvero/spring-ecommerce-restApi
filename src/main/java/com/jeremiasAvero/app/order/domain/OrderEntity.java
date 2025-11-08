package com.jeremiasAvero.app.order.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name="uk_orders_cart", columnNames = {"cartId"}),
                @UniqueConstraint(name="uk_orders_order_number", columnNames = {"orderNumber"})
        }
)
public class OrderEntity {
    @Id @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 40)
    private String orderNumber;

    private Long userId;

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public FulfillmentStatus getFulfillmentStatus() {
        return fulfillmentStatus;
    }

    public void setFulfillmentStatus(FulfillmentStatus fulfillmentStatus) {
        this.fulfillmentStatus = fulfillmentStatus;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getShipping() {
        return shipping;
    }

    public void setShipping(BigDecimal shipping) {
        this.shipping = shipping;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
