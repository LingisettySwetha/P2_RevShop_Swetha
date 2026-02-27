package com.rev.app.controller;

import com.rev.app.service.ICartService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartPageController {

    @Autowired
    private ICartService cartService;

    
    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/cart";
        }

        cartService.addToCart(userId, productId);

        return "redirect:/cart";
    }

    
    @GetMapping
    public String viewCart(HttpSession session,
                           Model model) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null)
            return "redirect:/login";

        model.addAttribute("cartItems",
                cartService.viewCart(userId));

        return "cart";
    }

    
    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id) {

        cartService.removeItem(id);
        return "redirect:/cart";
    }
}