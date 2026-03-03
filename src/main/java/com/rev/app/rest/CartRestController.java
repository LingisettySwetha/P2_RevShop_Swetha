package com.rev.app.rest;

import com.rev.app.entity.CartItem;
import com.rev.app.exception.UnauthorizedException;
import com.rev.app.service.ICartService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/cart")
public class CartRestController {

    @Autowired
    private ICartService cartService;

    
    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId,
                            HttpSession session) {
        log.info("REST: Adding product {} to cart", productId);

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null)
            throw new UnauthorizedException("Login required");

        cartService.addToCart(userId, productId);

        return "Added to cart";
    }

    
    @GetMapping
    public List<CartItem> viewCart(HttpSession session) {

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null)
            throw new UnauthorizedException("Login required");

        return cartService.viewCart(userId);
    }

    
    @DeleteMapping("/{cartItemId}")
    public String removeItem(@PathVariable Long cartItemId,
                             HttpSession session) {

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null)
            throw new UnauthorizedException("Login required");

        cartService.removeItem(cartItemId);

        return "Item removed";
    }
}