package com.rev.app.rest;

import com.rev.app.entity.Category;
import com.rev.app.entity.Product;
import com.rev.app.entity.Seller;
import com.rev.app.exception.ResourceNotFoundException;
import com.rev.app.repository.ISellerRepository;
import com.rev.app.service.ICategoryService;
import com.rev.app.service.IProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ISellerRepository sellerRepository;

    
    @GetMapping
    public List<Product> getAllProducts() {
        log.info("REST: Fetching all products");
        return productService.getAllProducts();
    }

   
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
       return productService.getProductById(id);
   }

   
    @PostMapping("/add")
    public Product createProduct(@RequestBody Map<String, Object> payload) {
        Product product = new Product();
        product.setName((String) payload.get("name"));
        product.setDescription((String) payload.get("description"));
        product.setPrice(Double.valueOf(payload.get("price").toString()));
        product.setQuantity(Integer.valueOf(payload.get("quantity").toString()));

        if (payload.containsKey("discountPrice") && payload.get("discountPrice") != null) {
            product.setDiscountPrice(Double.valueOf(payload.get("discountPrice").toString()));
        }

        
        Long categoryId = Long.valueOf(payload.get("categoryId").toString());
        Category category = categoryService.getCategoryById(categoryId);
        product.setCategory(category);

        
        Long sellerId = Long.valueOf(payload.get("sellerId").toString());
        Seller seller = sellerRepository.findByUserUserId(sellerId)
                .or(() -> sellerRepository.findById(sellerId))
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + sellerId));
        product.setSeller(seller);

        log.info("REST: Creating product '{}' in category '{}' for seller {}", 
                product.getName(), category.getCategoryName(), sellerId);
        return productService.saveProduct(product);
    }

   
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id,
                                 @RequestBody Map<String, Object> payload) {
        Product product = productService.getProductById(id);

        if (payload.containsKey("name")) product.setName((String) payload.get("name"));
        if (payload.containsKey("description")) product.setDescription((String) payload.get("description"));
        if (payload.containsKey("price")) product.setPrice(Double.valueOf(payload.get("price").toString()));
        if (payload.containsKey("quantity")) product.setQuantity(Integer.valueOf(payload.get("quantity").toString()));
        if (payload.containsKey("discountPrice") && payload.get("discountPrice") != null) {
            product.setDiscountPrice(Double.valueOf(payload.get("discountPrice").toString()));
        }
        if (payload.containsKey("categoryId")) {
            Long categoryId = Long.valueOf(payload.get("categoryId").toString());
            Category category = categoryService.getCategoryById(categoryId);
            product.setCategory(category);
        }

        if (payload.containsKey("sellerId")) {
            Long sellerId = Long.valueOf(payload.get("sellerId").toString());
            Seller seller = sellerRepository.findByUserUserId(sellerId)
                    .or(() -> sellerRepository.findById(sellerId))
                    .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + sellerId));
            product.setSeller(seller);
        }

        return productService.saveProduct(product);
    }

    
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);
        return "Product deleted successfully";
    }
}
