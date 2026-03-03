package com.rev.app.service;

import com.rev.app.entity.*;
import com.rev.app.exception.InvalidRequestException;
import com.rev.app.exception.ResourceNotFoundException;
import com.rev.app.repository.ICartRepository;
import com.rev.app.repository.IProductRepository;
import com.rev.app.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private ICartRepository cartRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IProductRepository productRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private User user;
    private Product product;
    private Long userId = 1L;
    private Long productId = 101L;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(userId);

        product = new Product();
        product.setProductId(productId);
        product.setQuantity(10);
    }

    @Test
    void testAddToCart_NewCart() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.findByUser_UserId(userId)).thenReturn(null);
        
        Cart newCart = new Cart();
        newCart.setUser(user);
        when(cartRepository.save(any(Cart.class))).thenReturn(newCart);

        cartService.addToCart(userId, productId);

        verify(cartRepository, times(2)).save(any(Cart.class));
    }

    @Test
    void testAddToCart_ProductOutOfStock() {
        product.setQuantity(0);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.findByUser_UserId(userId)).thenReturn(new Cart());

        assertThrows(InvalidRequestException.class, () -> cartService.addToCart(userId, productId));
    }

    @Test
    void testViewCart_Success() {
        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser_UserId(userId)).thenReturn(cart);

        List<CartItem> items = cartService.viewCart(userId);

        assertNotNull(items);
        verify(cartRepository).findByUser_UserId(userId);
    }

    @Test
    void testRemoveItem_NotFound() {
        when(cartRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class, () -> cartService.removeItem(999L));
    }
}
