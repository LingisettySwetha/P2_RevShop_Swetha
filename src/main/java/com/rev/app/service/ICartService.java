package com.rev.app.service;

import com.rev.app.dto.CartRequest;
import com.rev.app.entity.CartItem;

import java.util.List;

public interface ICartService {



    List<CartItem> viewCart(Long userId);

    void updateQuantity(Long cartItemId, int quantity);

    void removeItem(Long cartItemId);

	void addToCart(Long userId, Long productId);
}