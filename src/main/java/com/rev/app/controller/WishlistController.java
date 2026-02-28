package com.rev.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rev.app.service.IWishlistService;


import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private IWishlistService wishlistService;

    @PostMapping("/add/{productId}")
    public String addToWishlist(@PathVariable Long productId,
                                HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        wishlistService.addToWishlist(userId, productId);

        return "redirect:/products";
    }

    @GetMapping
    public String viewWishlist(HttpSession session, Model model) {

        Long userId = (Long) session.getAttribute("userId");

        model.addAttribute("wishlist",
                wishlistService.getWishlist(userId));

        return "wishlist";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id) {

        wishlistService.removeFromWishlist(id);
        return "redirect:/wishlist";
    }
}
