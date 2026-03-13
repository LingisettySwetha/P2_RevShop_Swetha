package com.rev.app.rest;

import com.rev.app.dto.OrderDTO;
import com.rev.app.dto.PlaceOrderRequest;
import com.rev.app.entity.Order;
import com.rev.app.exception.InvalidRequestException;
import com.rev.app.exception.UnauthorizedException;
import com.rev.app.mapper.OrderMapper;
import com.rev.app.service.IOrderService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    @Autowired
    private IOrderService orderService;

    @PostMapping("/place")
    public OrderDTO placeOrder(@RequestBody PlaceOrderRequest request,
                               HttpSession session) {
        log.info("REST: Placing order for current user");

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null)
            throw new UnauthorizedException("Login required");

        String role = (String) session.getAttribute("role");
        if (!"BUYER".equals(role)) {
            throw new UnauthorizedException("Only buyers can place orders");
        }
        if (request == null || request.getAddress() == null || request.getAddress().isBlank()) {
            throw new InvalidRequestException("Shipping address is required");
        }

        Order order = orderService.placeOrder(userId, request.getAddress());
        return OrderMapper.toDTO(order);
    }

    @GetMapping("/history")
    public List<OrderDTO> getOrderHistory(HttpSession session) {

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null)
            throw new UnauthorizedException("Login required");

        return orderService.getOrderHistory(userId)
                .stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{orderId}")
    public OrderDTO getOrder(@PathVariable Long orderId,
                             HttpSession session) {

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null)
            throw new UnauthorizedException("Login required");

        String role =
                (String) session.getAttribute("role");

        if ("ADMIN".equals(role)) {
            return OrderMapper.toDTO(orderService.getOrderById(orderId));
        }
        return OrderMapper.toDTO(orderService.getOrderByIdForUser(orderId, userId));
    }

    @PutMapping("/cancel/{orderId}")
    public OrderDTO cancelOrder(@PathVariable Long orderId,
                                HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new UnauthorizedException("Login required");
        }
        String role = (String) session.getAttribute("role");
        if (!"BUYER".equals(role)) {
            throw new UnauthorizedException("Only buyers can cancel orders");
        }

        return OrderMapper.toDTO(orderService.cancelOrder(orderId, userId));
    }
}
