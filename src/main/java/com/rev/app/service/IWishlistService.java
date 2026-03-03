package com.rev.app.service;

import java.util.List;

import com.rev.app.dto.WishlistDTO;
import com.rev.app.entity.Wishlist;

public interface IWishlistService {

    void addToWishlist(Long userId, Long productId);

    List<WishlistDTO> getWishlist(Long userId); 

    

    void removeFromWishlist(Long wishlistId);
}