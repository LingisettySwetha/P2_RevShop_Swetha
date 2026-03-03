package com.rev.app.rest;

import com.rev.app.entity.Order;
import com.rev.app.entity.OrderItem;
import com.rev.app.exception.UnauthorizedException;
import com.rev.app.service.IOrderService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    @Autowired
    private IOrderService orderService;

    
    @PostMapping("/place")
    public String placeOrder(HttpSession session) {
        log.info("REST: Placing order for current user");

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null)
            throw new UnauthorizedException("Login required");

        orderService.placeOrder(userId, "API Order (No Address Provided)");

        return "Order placed successfully";
    }

    
    @GetMapping
    public List<Order> getOrders(HttpSession session) {

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null)
            throw new UnauthorizedException("Login required");

        return orderService.getOrdersByUser(userId);
    }

    
    @GetMapping("/{orderId}")
    public List<OrderItem> getOrderItems(@PathVariable Long orderId,
                                         HttpSession session) {

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null)
            throw new UnauthorizedException("Login required");

        return orderService.getOrderItems(orderId);
    }

    
    @PutMapping("/{orderId}/status")
    public String updateStatus(@PathVariable Long orderId,
                               @RequestParam String status,
                               HttpSession session) {

        String role =
                (String) session.getAttribute("role");

        if (role == null || !role.equals("ADMIN"))
            throw new UnauthorizedException("Admin access only");

        orderService.updateOrderStatus(orderId, status);

        return "Order status updated";
    }

   
    @DeleteMapping("/{orderId}")
    public String deleteOrder(@PathVariable Long orderId,
                              HttpSession session) {

        String role =
                (String) session.getAttribute("role");

        if (role == null || !role.equals("ADMIN"))
            throw new UnauthorizedException("Admin access only");

        orderService.deleteOrder(orderId);

        return "Order deleted";
    }
}