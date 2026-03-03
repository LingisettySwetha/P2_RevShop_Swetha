package com.rev.app.service;

import com.rev.app.entity.Product;
import com.rev.app.exception.ResourceNotFoundException;
import com.rev.app.repository.IProductRepository;
import com.rev.app.service.IProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        log.info("Saving product: {}", product.getName());
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        log.info("Fetching product with id: {}", id);

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id));
    }

    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);
        productRepository.deleteById(id);
    }
    @Override
    public List<Product> searchProducts(String keyword, Long categoryId) {
        return productRepository.searchProducts(keyword, categoryId);
    }

    @Override
    public List<Product> getProductsBySeller(Long userId) {
        return productRepository.findBySeller_User_UserId(userId);
    }
}
