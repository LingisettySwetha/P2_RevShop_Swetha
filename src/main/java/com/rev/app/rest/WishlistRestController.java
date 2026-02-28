package com.rev.app.rest;

import com.rev.app.dto.WishlistDTO;
import com.rev.app.entity.Wishlist;
import com.rev.app.service.IWishlistService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistRestController {

    @Autowired
    private IWishlistService wishlistService;

   
    @PostMapping("/add")
    public String add(@RequestParam Long userId,
                      @RequestParam Long productId) {

        wishlistService.addToWishlist(userId, productId);

        return "Added to wishlist successfully";
    }

    
    @GetMapping("/{userId}")
    public List<WishlistDTO> getWishlist(@PathVariable Long userId) {

        return wishlistService.getWishlist(userId);
    }

    
    @DeleteMapping("/remove/{wishlistId}")
    public String remove(@PathVariable Long wishlistId) {

        wishlistService.removeFromWishlist(wishlistId);

        return "Removed successfully";
    }
}