package com.rev.app.repository;

import com.rev.app.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {

    // Find products by category
    List<Product> findByCategoryCategoryId(Long categoryId);

    // Find products by seller
    List<Product> findBySellerSellerId(Long sellerId);

    // Search products by name (for search feature later)
    List<Product> findByNameContainingIgnoreCase(String name);
}