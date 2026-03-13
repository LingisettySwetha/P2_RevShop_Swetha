package com.rev.app.service;

import com.rev.app.entity.*;
import com.rev.app.exception.InvalidRequestException;
import com.rev.app.exception.ResourceNotFoundException;
import com.rev.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IOrderItemRepository orderItemRepository;

    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private ICartItemRepository cartItemRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IProductRepository productRepository;

    @Transactional
    @Override
    public Order placeOrder(Long userId, String address) {
        log.info("Placing order for user ID: {} to address: {}", userId, address);

        if (address == null || address.isBlank()) {
            throw new InvalidRequestException("Shipping address is required");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser_UserId(userId);

        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new InvalidRequestException("Cart is empty");
        }

        double calculatedTotal = 0.0;
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new InvalidRequestException("Insufficient stock for product: " + product.getName());
            }
            calculatedTotal += product.getPrice() * cartItem.getQuantity();
        }

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(address.trim());
        order.setOrderNumber(generateOrderNumber());
        order.setTotalAmount(calculatedTotal);
        order.setOrderStatus(OrderStatus.PLACED);
        order = orderRepository.save(order);

        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();

            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setSubtotal(product.getPrice() * cartItem.getQuantity());

            orderItemRepository.save(orderItem);
            order.getOrderItems().add(orderItem);
        }

        cartItemRepository.deleteAll(cart.getCartItems());

        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrderHistory(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return orderRepository.findByUser_UserIdOrderByOrderDateDesc(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItem> getOrderItems(Long orderId) {
        orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        return orderItemRepository.findByOrder_OrderId(orderId);
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        if (status == null) {
            throw new InvalidRequestException("Order status is required");
        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        order.setOrderStatus(status);
        orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order not found with id: {}", orderId);
                    return new ResourceNotFoundException("Order not found");
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderByIdForUser(Long orderId, Long userId) {
        Order order = getOrderById(orderId);
        if (!order.getUser().getUserId().equals(userId)) {
            throw new InvalidRequestException("Order does not belong to user");
        }
        return order;
    }

    @Override
    @Transactional
    public Order cancelOrder(Long orderId, Long userId) {
        Order order = getOrderByIdForUser(orderId, userId);

        if (order.getOrderStatus() == OrderStatus.SHIPPED || order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new InvalidRequestException("Cancellation not allowed after shipping");
        }
        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            return order;
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    private String generateOrderNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String orderNumber;
        do {
            int suffix = (int) (Math.random() * 900000) + 100000;
            orderNumber = String.format(Locale.ROOT, "RS-%s-%d", datePart, suffix);
        } while (orderRepository.existsByOrderNumber(orderNumber));
        return orderNumber;
    }
}
