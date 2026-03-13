package com.rev.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name="order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    private Integer quantity;
    private Double price;
    private Double subtotal;

    public Long getOrderItemId(){ return orderItemId; }
    public void setOrderItemId(Long id){ this.orderItemId=id; }

    public Order getOrder(){ return order; }
    public void setOrder(Order order){ this.order=order; }

    public Product getProduct(){ return product; }
    public void setProduct(Product product){ this.product=product; }

    public Integer getQuantity(){ return quantity; }
    public void setQuantity(Integer quantity){ this.quantity=quantity; }

    public Double getPrice(){ return price; }
    public void setPrice(Double price){ this.price=price; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
}
