package com.rev.app.controller;

import com.rev.app.exception.UnauthorizedException;
import com.rev.app.service.IDashboardService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AdminDashboardController {

    @Autowired
    private IDashboardService dashboardService;

    @GetMapping("/admin/dashboard")
    public String dashboard(HttpSession session,
                            Model model) {
        log.info("Accessing Admin Dashboard");

        String role = (String) session.getAttribute("role");

        if (role == null || !role.equals("ADMIN"))
            throw new UnauthorizedException("Admin access only");

        model.addAttribute("users",
                dashboardService.totalUsers());
        model.addAttribute("products",
                dashboardService.totalProducts());
        model.addAttribute("orders",
                dashboardService.totalOrders());
        model.addAttribute("revenue",
                dashboardService.totalRevenue());

        return "admin-dashboard";
    }
}