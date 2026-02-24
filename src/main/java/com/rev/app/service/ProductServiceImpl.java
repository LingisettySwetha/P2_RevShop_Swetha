package com.rev.app.service;

import com.rev.app.dto.ProductRequest;
import com.rev.app.entity.Category;
import com.rev.app.entity.Product;
import com.rev.app.entity.Seller;
import com.rev.app.mapper.ProductMapper;
import com.rev.app.repository.ICategoryRepository;
import com.rev.app.repository.IProductRepository;
import com.rev.app.repository.ISellerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private ISellerRepository sellerRepository;

    @Autowired
    private ICategoryRepository categoryRepository;

    // ================= ADD PRODUCT =================
    @Override
    public Product addProduct(ProductRequest request) {

        Seller seller = sellerRepository.findById(request.getSellerId())
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product =
                ProductMapper.toEntity(request, seller, category);

        return productRepository.save(product);
    }

    // ================= VIEW ALL PRODUCTS =================
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ================= PRODUCTS BY CATEGORY =================
    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryCategoryId(categoryId);
    }

    // ================= PRODUCTS BY SELLER =================
    @Override
    public List<Product> getProductsBySeller(Long sellerId) {
        return productRepository.findBySellerSellerId(sellerId);
    }

    // ================= DELETE PRODUCT =================
    @Override
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
}