package com.rev.app.service;

import com.rev.app.entity.Seller;
import com.rev.app.entity.User;
import com.rev.app.exception.ResourceNotFoundException;
import com.rev.app.repository.*;
import com.rev.app.entity.*;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@Service
public class SellerServiceImpl implements ISellerService {

    @Autowired
    private ISellerRepository sellerRepository;

    @Autowired
    private IOrderItemRepository orderItemRepository;

    @Autowired
    private IPaymentRepository paymentRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Override
    public void createSeller(User user, String businessName) {
        Seller existing = sellerRepository.findByUserUserId(user.getUserId()).orElse(null);
        if (existing != null) {
            log.info("Seller profile already exists for user {}", user.getUserId());
            return;
        }

        log.info("Creating seller for user {} with business name: {}", user.getEmail(), businessName);
        Seller seller = new Seller();
        seller.setUser(user);
        seller.setBusinessName(businessName);
        try {
            sellerRepository.save(seller);
        } catch (DataIntegrityViolationException ex) {
            
            log.warn("Seller profile creation hit integrity constraint for user {}. Reusing existing profile.",
                    user.getUserId());
        }
    }

    @Override
    public List<OrderItem> getSellerOrders(Long userId) {
        log.info("Fetching orders for seller user ID: {}", userId);
        return orderItemRepository.findSellerOrderItems(userId);
    }

    @Override
    public double calculateTotalRevenue(Long userId) {
        log.info("Calculating total revenue for seller user ID: {}", userId);
        return orderItemRepository.findSellerOrderItems(userId)
                .stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    @Override
    public void shipOrderItem(Long orderItemId) {
        log.info("Shipping order item ID: {}", orderItemId);
        OrderItem item = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found"));
        Order order = item.getOrder();
        order.setOrderStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);
    }

    @Override
    public void updatePaymentStatus(Long orderId, String status) {
        log.info("Updating payment status for order ID: {} to {}", orderId, status);
        Payment payment = paymentRepository.findByOrder_OrderId(orderId);
        if (payment != null) {
            payment.setPaymentStatus(status);
            paymentRepository.save(payment);
        } else {
            log.warn("No payment found for order ID: {}", orderId);
        }
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        log.info("Updating order status for order ID: {} to {}", orderId, status);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setOrderStatus(status);
        orderRepository.save(order);
    }
}
