package com.rev.app.controller;

import com.rev.app.entity.Order;
import com.rev.app.entity.OrderItem;
import com.rev.app.entity.OrderStatus;
import com.rev.app.exception.UnauthorizedException;
import com.rev.app.service.IOrderService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private IOrderService orderService;

   
    @PostMapping("/place")
    public String placeOrder(HttpSession session) {
        log.info("Placing order from web UI");

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null)
            return "redirect:/login";

        String role = (String) session.getAttribute("role");
        if (!"BUYER".equals(role)) {
            log.warn("Non-buyer user {} attempted to place order", userId);
            throw new UnauthorizedException("Only buyers can place orders");
        }

        orderService.placeOrder(userId, "Web Order");
        session.setAttribute("cartCount", 0);

        return "redirect:/orders/success";
    }

  
    @GetMapping("/success")
    public String orderSuccess(HttpSession session) {

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null)
            return "redirect:/login";

        return "order-success";
    }

    
    @GetMapping
    public String viewOrders(HttpSession session,
                             Model model) {

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null)
            return "redirect:/login";

        List<Order> orders = orderService.getOrderHistory(userId);

        model.addAttribute("orders", orders);

        return "orders";
    }

   
    @GetMapping("/{orderId}")
    public String orderDetails(@PathVariable Long orderId,
                               HttpSession session,
                               Model model) {

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null)
            return "redirect:/login";

        Order order = orderService.getOrderByIdForUser(orderId, userId);

        List<OrderItem> items =
                orderService.getOrderItems(orderId);

        model.addAttribute("orderId", orderId);
        model.addAttribute("order", order);
        model.addAttribute("items", items);
        model.addAttribute("orderItems", items);

        return "order-details";
    }

    @PostMapping("/{orderId}/cancel")
    public String cancelOrder(@PathVariable Long orderId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        String role = (String) session.getAttribute("role");
        if (!"BUYER".equals(role)) throw new UnauthorizedException("Access denied");

        Order order = orderService.getOrderByIdForUser(orderId, userId);
        if (order.getOrderStatus() == OrderStatus.PLACED || order.getOrderStatus() == OrderStatus.CONFIRMED) {
            orderService.cancelOrder(orderId, userId);
        }
        return "redirect:/orders";
    }
}
