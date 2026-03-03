package com.rev.app.rest;

import com.rev.app.entity.Product;
import com.rev.app.service.IProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductRestControllerTest {

    @Mock
    private IProductService productService;

    @InjectMocks
    private ProductRestController productRestController;

    @Test
    void testGetAllProducts() {
        when(productService.getAllProducts()).thenReturn(List.of(new Product()));
        List<Product> result = productRestController.getAllProducts();
        assertEquals(1, result.size());
    }
}

