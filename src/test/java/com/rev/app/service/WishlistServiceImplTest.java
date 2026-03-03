package com.rev.app.service;

import com.rev.app.entity.Product;
import com.rev.app.entity.User;
import com.rev.app.entity.Wishlist;
import com.rev.app.repository.IProductRepository;
import com.rev.app.repository.IUserRepository;
import com.rev.app.repository.IWishlistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WishlistServiceImplTest {

    @Mock
    private IWishlistRepository wishlistRepository;
    @Mock
    private IUserRepository userRepository;
    @Mock
    private IProductRepository productRepository;

    @InjectMocks
    private WishlistServiceImpl wishlistService;

    @Test
    void testAddToWishlist_Duplicate() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(productRepository.findById(1L)).thenReturn(Optional.of(new Product()));
        when(wishlistRepository.findByUser_UserIdAndProduct_ProductId(1L, 1L))
                .thenReturn(Optional.of(new Wishlist()));

        assertDoesNotThrow(() -> wishlistService.addToWishlist(1L, 1L));
    }
}
