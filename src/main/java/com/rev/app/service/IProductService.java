package com.rev.app.service;

import com.rev.app.dto.ProductRequest;
import com.rev.app.entity.Product;

import java.util.List;

public interface IProductService {

    Product addProduct(ProductRequest request);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(Long categoryId);

    List<Product> getProductsBySeller(Long sellerId);

    void deleteProduct(Long productId);
}