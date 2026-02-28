package com.rev.app.controller;

import com.rev.app.service.ICartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheckoutController {

    @Autowired
    private ICartService cartService;

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null)
            return "redirect:/login";

        model.addAttribute("cartItems",
                cartService.viewCart(userId));

        return "checkout";
    }
}