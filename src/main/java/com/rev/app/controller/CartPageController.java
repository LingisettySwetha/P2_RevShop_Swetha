package com.rev.app.controller;

import com.rev.app.service.ICartService;
import com.rev.app.service.IOrderService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/cart")
public class CartPageController {

    @Autowired
    private ICartService cartService;

    @Autowired
    private IOrderService orderService;

    
    private boolean isSeller(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return "SELLER".equals(role);
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        if (isSeller(session)) {
            log.warn("Seller {} attempted to add to cart – blocked", userId);
            return "redirect:/seller/dashboard";
        }
        log.info("Adding product {} to cart for user {}", productId, userId);
        cartService.addToCart(userId, productId);
        session.setAttribute("cartCount", cartService.viewCart(userId).size());
        return "redirect:/cart";
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        if (isSeller(session)) {
            log.warn("Seller {} attempted to view cart – blocked", userId);
            return "redirect:/seller/dashboard";
        }
        var cartItems = cartService.viewCart(userId);
        model.addAttribute("cartItems", cartItems);
        session.setAttribute("cartCount", cartItems.size());
        return "cart";
    }

    @PostMapping("/update/{id}")
    public String updateQuantity(@PathVariable Long id, @RequestParam int quantity, HttpSession session) {
        if (isSeller(session)) return "redirect:/seller/dashboard";
        cartService.updateQuantity(id, quantity);
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            session.setAttribute("cartCount", cartService.viewCart(userId).size());
        }
        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id, HttpSession session) {
        if (isSeller(session)) return "redirect:/seller/dashboard";
        cartService.removeItem(id);
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            session.setAttribute("cartCount", cartService.viewCart(userId).size());
        }
        return "redirect:/cart";
    }

    @PostMapping("/placeOrder")
    public String placeOrder(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        if (isSeller(session)) {
            log.warn("Seller {} attempted to place order – blocked", userId);
            return "redirect:/seller/dashboard";
        }
        log.info("Placing order for user {}", userId);
        orderService.placeOrder(userId, "Cart Page Quick Order");
        session.setAttribute("cartCount", 0);
        return "redirect:/orders/success";
    }
}
