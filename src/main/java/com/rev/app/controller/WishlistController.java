package com.rev.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.rev.app.service.IWishlistService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private IWishlistService wishlistService;

    
    @PostMapping("/add/{productId}")
    public String addToWishlist(@PathVariable Long productId,
                                HttpSession session) {
        log.info("UI: Adding product {} to wishlist", productId);

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null)
            return "redirect:/login";

        wishlistService.addToWishlist(userId, productId);

        return "redirect:/products";
    }

   
    @GetMapping
    public String viewWishlist(HttpSession session, Model model) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null)
            return "redirect:/login";

        model.addAttribute("wishlist",
                wishlistService.getWishlist(userId));

        return "wishlist";
    }

    
    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id,
                         HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null)
            return "redirect:/login";

        wishlistService.removeFromWishlist(id);
        return "redirect:/wishlist";
    }
}
