package com.rev.app.service;

import com.rev.app.entity.*;
import com.rev.app.repository.*;
import com.rev.app.service.IOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

  
    @Override
    public void placeOrder(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser_UserId(userId);

        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        
        Order order = new Order();
        order.setUser(user);

        double totalAmount = 0.0;

        order = orderRepository.save(order);

        
        for (CartItem cartItem : cart.getCartItems()) {

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());

            totalAmount +=
                    cartItem.getProduct().getPrice()
                            * cartItem.getQuantity();

            orderItemRepository.save(orderItem);
        }

        order.setTotalAmount(totalAmount);
        orderRepository.save(order);

        // Clear Cart
        cartItemRepository.deleteAll(cart.getCartItems());
    }

   
    @Override
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUser_UserId(userId);
    }

    @Override
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrder_OrderId(orderId);
    }

    
    @Override
    public void updateOrderStatus(Long orderId, String status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderStatus(status);
        orderRepository.save(order);
    }

    
    @Override
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
    
    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    
}