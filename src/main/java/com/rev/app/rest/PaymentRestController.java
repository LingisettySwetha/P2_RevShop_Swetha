package com.rev.app.rest;

import com.rev.app.dto.PaymentCheckoutRequest;
import com.rev.app.entity.Order;
import com.rev.app.entity.Payment;
import com.rev.app.exception.UnauthorizedException;
import com.rev.app.service.IOrderService;
import com.rev.app.service.IPaymentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentRestController {

    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private IOrderService orderService;

    @PostMapping("/checkout")
    public Map<String, Object> checkout(@RequestBody PaymentCheckoutRequest request,
                                        HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new UnauthorizedException("Login required");
        }

        String role = (String) session.getAttribute("role");
        if (!"BUYER".equals(role)) {
            throw new UnauthorizedException("Only buyers can checkout");
        }

        Order order = paymentService.checkoutWithPayment(
                userId,
                request.getAddress(),
                request.getPaymentMethod(),
                request.getCardHolderName(),
                request.getCardType(),
                request.getCardNumber(),
                request.getExpiryDate(),
                request.getCvv());

        session.setAttribute("cartCount", 0);

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", order.getOrderId());
        response.put("orderNumber", order.getOrderNumber());
        response.put("orderStatus", order.getOrderStatus() != null ? order.getOrderStatus().name() : null);
        response.put("paymentMethod", order.getPayment() != null ? order.getPayment().getPaymentMethod() : null);
        response.put("paymentStatus", order.getPayment() != null ? order.getPayment().getPaymentStatus() : "PENDING");
        response.put("message", "Checkout completed");
        return response;
    }

    @GetMapping("/order/{orderId}")
    public Payment getPaymentByOrder(@PathVariable Long orderId,
                                     HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new UnauthorizedException("Login required");
        }

        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            Order order = orderService.getOrderById(orderId);
            if (!order.getUser().getUserId().equals(userId)) {
                throw new UnauthorizedException("Access denied");
            }
        }

        return paymentService.getPaymentByOrderId(orderId);
    }
}
