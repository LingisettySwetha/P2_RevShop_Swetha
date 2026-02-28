package com.rev.app.controller;

import com.rev.app.service.IOrderService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    
    @GetMapping
    public String viewOrders(HttpSession session, Model model) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null)
            return "redirect:/login";

        model.addAttribute("orders",
                orderService.getOrdersByUser(userId));

        return "orders";
    }

   
    @GetMapping("/{orderId}")
    public String orderDetails(@PathVariable Long orderId,
                               HttpSession session,
                               Model model) {

        if (session.getAttribute("userId") == null)
            return "redirect:/login";

        model.addAttribute("orderItems",
                orderService.getOrderItems(orderId));

        model.addAttribute("orderId", orderId);

        return "order-details";
    }

    
    @PostMapping("/update-status")
    public String updateStatus(@RequestParam Long orderId,
                               @RequestParam String status) {

        orderService.updateOrderStatus(orderId, status);

        return "redirect:/orders";
    }

   
    @GetMapping("/delete/{orderId}")
    public String deleteOrder(@PathVariable Long orderId) {

        orderService.deleteOrder(orderId);

        return "redirect:/orders";
    }
}