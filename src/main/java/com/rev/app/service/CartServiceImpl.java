package com.rev.app.service;

import com.rev.app.entity.*;
import com.rev.app.repository.ICartRepository;
import com.rev.app.repository.IProductRepository;
import com.rev.app.repository.IUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IProductRepository productRepository;

   
    @Override
    public void addToCart(Long userId, Long productId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUserUserId(userId);

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        CartItem existingItem = null;

        for (CartItem item : cart.getCartItems()) {
            if (item.getProduct().getProductId().equals(productId)) {
                existingItem = item;
                break;
            }
        }

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(1);
            cart.getCartItems().add(newItem);
        }

        cartRepository.save(cart);
    }
    
    @Override
    public List<CartItem> viewCart(Long userId) {

        Cart cart = cartRepository.findByUserUserId(userId);

        if (cart == null) {
            return List.of();
        }

        return cart.getCartItems();
    }

   
    @Override
    public void removeItem(Long cartItemId) {

        List<Cart> carts = cartRepository.findAll();

        for (Cart cart : carts) {

            cart.getCartItems()
                    .removeIf(item ->
                            item.getCartItemId().equals(cartItemId));

            cartRepository.save(cart);
        }
    }
}
