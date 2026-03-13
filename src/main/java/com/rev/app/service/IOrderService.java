package com.rev.app.service;

import com.rev.app.entity.Order;
import com.rev.app.entity.OrderItem;
import com.rev.app.entity.OrderStatus;

import java.util.List;

public interface IOrderService {

    Order placeOrder(Long userId, String address);

    List<Order> getOrderHistory(Long userId);

    List<OrderItem> getOrderItems(Long orderId);

    void updateOrderStatus(Long orderId, OrderStatus status);

    Order getOrderById(Long orderId);

    Order getOrderByIdForUser(Long orderId, Long userId);

    Order cancelOrder(Long orderId, Long userId);
}
