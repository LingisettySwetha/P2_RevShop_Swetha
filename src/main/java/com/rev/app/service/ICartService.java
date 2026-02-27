package com.rev.app.service;

import com.rev.app.dto.CartRequest;
import com.rev.app.entity.CartItem;

import java.util.List;

public interface ICartService {

//    void addToCart(CartRequest request);

    List<CartItem> viewCart(Long userId);

    void removeItem(Long cartItemId);

	void addToCart(Long userId, Long productId);
}