package com.rev.app.rest;

import com.rev.app.entity.Order;
import com.rev.app.entity.OrderItem;
import com.rev.app.service.IOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    @Autowired
    private IOrderService orderService;

    
    @PostMapping("/checkout/{userId}")
    public String checkout(@PathVariable Long userId) {

        orderService.placeOrder(userId);

        return "Order placed successfully";
    }

    
    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUser(
            @PathVariable Long userId) {

        return orderService.getOrdersByUser(userId);
    }

    
    @GetMapping("/{orderId}")
    public List<OrderItem> getOrderDetails(
            @PathVariable Long orderId) {

        return orderService.getOrderItems(orderId);
    }

    
    @PutMapping("/update-status")
    public String updateStatus(@RequestParam Long orderId,
                               @RequestParam String status) {

        orderService.updateOrderStatus(orderId, status);

        return "Order status updated";
    }

    
    @DeleteMapping("/{orderId}")
    public String deleteOrder(@PathVariable Long orderId) {

        orderService.deleteOrder(orderId);

        return "Order deleted successfully";
    }
}