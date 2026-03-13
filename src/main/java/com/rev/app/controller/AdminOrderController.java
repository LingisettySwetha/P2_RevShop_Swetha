package com.rev.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.rev.app.entity.OrderStatus;
import com.rev.app.exception.UnauthorizedException;
import com.rev.app.service.IOrderService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private IOrderService orderService;

    @PostMapping("/update-status")
    public String updateStatus(@RequestParam Long orderId,
                               @RequestParam OrderStatus status,
                               HttpSession session) {
        log.info("Admin: Updating order {} status to {}", orderId, status);

        String role = (String) session.getAttribute("role");

        if (role == null || !role.equals("ADMIN"))
            throw new UnauthorizedException("Admin access only");

        orderService.updateOrderStatus(orderId, status);

        return "redirect:/admin/dashboard";
    }
}
