package com.rev.app.service;

import com.rev.app.entity.*;
import com.rev.app.exception.InvalidRequestException;
import com.rev.app.exception.ResourceNotFoundException;
import com.rev.app.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
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
        order.setShippingAddress(address);
        order.setTotalAmount(calculatedTotal);
        order.setOrderStatus("PLACED");
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

            orderItemRepository.save(orderItem);
        }

        
        cartItemRepository.deleteAll(cart.getCartItems());

        return order;
    }

    
    @Override
    public List<Order> getOrdersByUser(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return orderRepository.findByUser_UserId(userId);
    }

   
    @Override
    public List<OrderItem> getOrderItems(Long orderId) {

        orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        return orderItemRepository.findByOrder_OrderId(orderId);
    }

   
    @Override
    public void updateOrderStatus(Long orderId, String status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        order.setOrderStatus(status);
        orderRepository.save(order);
    }

    
    @Override
    public void deleteOrder(Long orderId) {

        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("Order not found");
        }

        orderRepository.deleteById(orderId);
    }

   
    @Override
    public Order getOrderById(Long orderId) {

        return orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order not found with id: {}", orderId);
                    return new ResourceNotFoundException("Order not found");
                });
    }
}