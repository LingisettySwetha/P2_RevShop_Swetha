package com.rev.app.service;

import com.rev.app.entity.Order;
import com.rev.app.entity.OrderItem;

import java.util.List;

public interface IOrderService {

   
    Order placeOrder(Long userId, String address);

    
    List<Order> getOrdersByUser(Long userId);

    List<OrderItem> getOrderItems(Long orderId);

 
    void updateOrderStatus(Long orderId, String status);

    
    void deleteOrder(Long orderId);
    
    Order getOrderById(Long orderId);
   
}