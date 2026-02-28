package com.rev.app.controller;

import com.rev.app.service.IDashboardService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {

    @Autowired
    private IDashboardService dashboardService;

    @GetMapping("/admin/dashboard")
    public String dashboard(HttpSession session, Model model) {

        String role = (String) session.getAttribute("role");

        if (role == null || !role.equals("ADMIN")) {
            return "redirect:/login";
        }

        model.addAttribute("users", dashboardService.totalUsers());
        model.addAttribute("products", dashboardService.totalProducts());
        model.addAttribute("orders", dashboardService.totalOrders());
        model.addAttribute("revenue", dashboardService.totalRevenue());

        return "admin-dashboard";
    }
}