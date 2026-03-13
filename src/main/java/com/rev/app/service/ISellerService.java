package com.rev.app.service;

import com.rev.app.entity.*;
import java.util.List;

public interface ISellerService {

    void createSeller(User user, String businessName);

    List<OrderItem> getSellerOrders(Long userId);

    double calculateTotalRevenue(Long userId);

    void shipOrderItem(Long orderItemId);

    void updatePaymentStatus(Long orderId, String status);

    void updateOrderStatus(Long orderId, OrderStatus status);
}
