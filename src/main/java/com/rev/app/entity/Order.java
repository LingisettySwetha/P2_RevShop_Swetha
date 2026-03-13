package com.rev.app.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "order_number", unique = true, nullable = false, length = 64)
    private String orderNumber;

    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    private LocalDateTime orderDate;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    @JsonIgnore
    @OneToMany(mappedBy="order",
            cascade=CascadeType.ALL,
            orphanRemoval=true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {
        this.orderStatus = OrderStatus.PLACED;
        this.orderDate=LocalDateTime.now();
    }

    public Long getOrderId(){ return orderId; }
    public void setOrderId(Long orderId){ this.orderId=orderId; }

    public User getUser(){ return user; }
    public void setUser(User user){ this.user=user; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public Double getTotalAmount(){ return totalAmount; }
    public void setTotalAmount(Double totalAmount){ this.totalAmount=totalAmount; }

    public OrderStatus getOrderStatus(){ return orderStatus; }
    public void setOrderStatus(OrderStatus orderStatus){ this.orderStatus=orderStatus; }

    public LocalDateTime getOrderDate(){ return orderDate; }

    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }

    public List<OrderItem> getOrderItems(){ return orderItems; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
}
