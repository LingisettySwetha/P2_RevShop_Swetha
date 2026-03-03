package com.rev.app.service;

import com.rev.app.entity.*;
import com.rev.app.exception.InvalidRequestException;
import com.rev.app.exception.ResourceNotFoundException;
import com.rev.app.repository.ICartRepository;
import com.rev.app.repository.IProductRepository;
import com.rev.app.repository.IUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        log.info("Adding product {} to cart for user {}", productId, userId);

       
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product not found with id: {}", productId);
                    return new ResourceNotFoundException("Product not found");
                });

        
        Cart cart = cartRepository.findByUser_UserId(userId);

        if (cart == null) {
            log.info("Creating new cart for user {}", userId);
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        
        CartItem existingItem = cart.getCartItems()
                .stream()
                .filter(item ->
                        item.getProduct().getProductId()
                                .equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {

            
            if (existingItem.getQuantity() >= product.getQuantity()) {
                throw new InvalidRequestException(
                        "Product stock limit reached");
            }

            existingItem.setQuantity(existingItem.getQuantity() + 1);

        } else {

            if (product.getQuantity() <= 0) {
                throw new InvalidRequestException(
                        "Product is out of stock");
            }

            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(1);

            cart.getCartItems().add(newItem);
        }

        cartRepository.save(cart);
        log.info("Successfully added product {} to cart for user {}", productId, userId);
    }

    
    @Override
    public List<CartItem> viewCart(Long userId) {
        log.info("Fetching cart items for user {}", userId);

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser_UserId(userId);

        if (cart == null) {
            return List.of();
        }

        return cart.getCartItems();
    }

    
    @Override
    public void updateQuantity(Long cartItemId, int quantity) {
        log.info("Updating cart item {} quantity to {}", cartItemId, quantity);

        if (quantity < 1) {
            removeItem(cartItemId);
            return;
        }

        List<Cart> carts = cartRepository.findAll();
        for (Cart cart : carts) {
            for (CartItem item : cart.getCartItems()) {
                if (item.getCartItemId().equals(cartItemId)) {
                    if (quantity > item.getProduct().getQuantity()) {
                        throw new InvalidRequestException("Requested quantity exceeds stock");
                    }
                    item.setQuantity(quantity);
                    cartRepository.save(cart);
                    return;
                }
            }
        }
        throw new ResourceNotFoundException("Cart item not found");
    }
    
    @Override
    public void removeItem(Long cartItemId) {
        log.info("Removing cart item with ID: {}", cartItemId);

        boolean found = false;

        List<Cart> carts = cartRepository.findAll();

        for (Cart cart : carts) {

            found = cart.getCartItems()
                    .removeIf(item ->
                            item.getCartItemId()
                                    .equals(cartItemId));

            if (found) {
                cartRepository.save(cart);
                return;
            }
        }

        throw new ResourceNotFoundException(
                "Cart item not found");
    }
}