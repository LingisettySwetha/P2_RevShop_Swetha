package com.rev.app.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {

    private Long orderId;
    private String orderNumber;
    private Double totalAmount;
    private String orderStatus;
    private LocalDateTime orderDate;
    private List<OrderItemDTO> items;

    public Long getOrderId(){ return orderId; }
    public void setOrderId(Long orderId){ this.orderId=orderId; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public Double getTotalAmount(){ return totalAmount; }
    public void setTotalAmount(Double totalAmount){ this.totalAmount=totalAmount; }

    public String getOrderStatus(){ return orderStatus; }
    public void setOrderStatus(String orderStatus){ this.orderStatus=orderStatus; }

    public LocalDateTime getOrderDate(){ return orderDate; }
    public void setOrderDate(LocalDateTime orderDate){ this.orderDate=orderDate; }

    public List<OrderItemDTO> getItems(){ return items; }
    public void setItems(List<OrderItemDTO> items){ this.items=items; }
}
