package com.rev.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rev.app.dto.WishlistDTO;
import com.rev.app.entity.Product;
import com.rev.app.entity.User;
import com.rev.app.entity.Wishlist;
import com.rev.app.exception.ResourceNotFoundException;
import com.rev.app.mapper.WishlistMapper;
import com.rev.app.repository.IProductRepository;
import com.rev.app.repository.IUserRepository;
import com.rev.app.repository.IWishlistRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WishlistServiceImpl implements IWishlistService {

    @Autowired
    private IWishlistRepository wishlistRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IProductRepository productRepository;

    
    @Override
    public void addToWishlist(Long userId, Long productId) {
        log.info("Adding product {} to wishlist for user {}", productId, userId);

       
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        
        if (wishlistRepository
                .findByUser_UserIdAndProduct_ProductId(userId, productId)
                .isPresent()) {

            return;
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);

        wishlistRepository.save(wishlist);
    }

    
    @Override
    public List<WishlistDTO> getWishlist(Long userId) {

        
        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return wishlistRepository
                .findByUser_UserId(userId)
                .stream()
                .map(WishlistMapper::toDTO)
                .toList();
    }

   
    @Override
    public void removeFromWishlist(Long wishlistId) {

        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Wishlist item not found"));

        wishlistRepository.delete(wishlist);
    }
}
