package com.rev.app.service;

import com.rev.app.entity.Product;
import java.util.List;

public interface IProductService {

    Product saveProduct(Product product);

    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product updateProduct(Product product);

    void deleteProduct(Long id);
    
    List<Product> searchProducts(String keyword, Long categoryId);
}