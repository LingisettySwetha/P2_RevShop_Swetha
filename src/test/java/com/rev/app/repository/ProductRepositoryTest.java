package com.rev.app.repository;

import com.rev.app.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @Mock
    private IProductRepository productRepository;

    @Test
    void testSearchProducts() {
        when(productRepository.searchProducts("phone", null))
                .thenReturn(List.of(new Product(), new Product()));

        List<Product> result = productRepository.searchProducts("phone", null);

        assertEquals(2, result.size());
    }

    @Test
    void testFindBySellerUserId() {
        when(productRepository.findBySeller_User_UserId(1L))
                .thenReturn(List.of(new Product()));

        List<Product> result = productRepository.findBySeller_User_UserId(1L);

        assertEquals(1, result.size());
    }
}
