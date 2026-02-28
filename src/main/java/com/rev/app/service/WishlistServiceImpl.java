package com.rev.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rev.app.dto.WishlistDTO;
import com.rev.app.entity.Wishlist;
import com.rev.app.mapper.WishlistMapper;
import com.rev.app.repository.IProductRepository;
import com.rev.app.repository.IUserRepository;
import com.rev.app.repository.IWishlistRepository;

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

	        if (wishlistRepository
	            .findByUser_UserIdAndProduct_ProductId(userId, productId)
	            .isPresent()) {
	            return;
	        }

	        Wishlist wishlist = new Wishlist();
	        wishlist.setUser(userRepository.findById(userId).get());
	        wishlist.setProduct(productRepository.findById(productId).get());

	        wishlistRepository.save(wishlist);
	    }

	    @Override
	    public List<WishlistDTO> getWishlist(Long userId) {

	        return wishlistRepository
	                .findByUser_UserId(userId)
	                .stream()
	                .map(WishlistMapper::toDTO)
	                .toList();
	    }

	    @Override
	    public void removeFromWishlist(Long wishlistId) {

	        if (!wishlistRepository.existsById(wishlistId)) {
	            throw new RuntimeException("Wishlist item not found");
	        }

	        wishlistRepository.deleteById(wishlistId);
	    }
}


