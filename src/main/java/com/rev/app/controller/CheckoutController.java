package com.rev.app.controller;

import com.rev.app.service.ICartService;
import com.rev.app.service.IPaymentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class CheckoutController {

    @Autowired
    private ICartService cartService;

    @Autowired
    private IPaymentService paymentService;

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        log.info("Accessing checkout page");

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null)
            return "redirect:/login";

        String role = (String) session.getAttribute("role");
        if (!"BUYER".equals(role)) {
            return "redirect:/";
        }

        var cartItems = cartService.viewCart(userId);
        model.addAttribute("cartItems", cartItems);

        
        double total = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        model.addAttribute("cartTotal", total);

        return "checkout";
    }

    @PostMapping("/orders/checkout")
    public String processCheckout(@RequestParam String address,
                                  @RequestParam(defaultValue = "CARD") String paymentMethod,
                                  @RequestParam(required = false) String cardNumber,
                                  @RequestParam(required = false) String expiryDate,
                                  @RequestParam(required = false) String cvv,
                                  HttpSession session) {
        log.info("Processing checkout / payment with address: {}", address);

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null)
            return "redirect:/login";

        String role = (String) session.getAttribute("role");
        if (!"BUYER".equals(role)) {
            return "redirect:/";
        }

        paymentService.checkoutWithPayment(
                userId,
                address,
                paymentMethod,
                cardNumber,
                expiryDate,
                cvv);
        session.setAttribute("cartCount", 0);

        return "redirect:/orders/success";
    }
}
