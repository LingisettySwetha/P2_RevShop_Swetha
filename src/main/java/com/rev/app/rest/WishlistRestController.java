package com.rev.app.rest;

import com.rev.app.dto.WishlistDTO;
import com.rev.app.exception.UnauthorizedException;
import com.rev.app.service.IWishlistService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/wishlist")
public class WishlistRestController {

    @Autowired
    private IWishlistService wishlistService;

    
    @PostMapping("/add/{productId}")
    public String addToWishlist(@PathVariable Long productId,
                                HttpSession session) {
        log.info("REST: Adding product {} to wishlist", productId);

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null)
            throw new UnauthorizedException("Login required");

        wishlistService.addToWishlist(userId, productId);

        return "Added to wishlist";
    }

    
    @GetMapping
    public List<WishlistDTO> viewWishlist(HttpSession session) {

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null)
            throw new UnauthorizedException("Login required");

        return wishlistService.getWishlist(userId);
    }

    
    @DeleteMapping("/{id}")
    public String remove(@PathVariable Long id,
                         HttpSession session) {

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null)
            throw new UnauthorizedException("Login required");

        wishlistService.removeFromWishlist(id);

        return "Removed from wishlist";
    }
}