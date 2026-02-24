package com.rev.app.rest;

import com.rev.app.dto.ProductRequest;
import com.rev.app.entity.Product;
import com.rev.app.service.IProductService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductRestController {

    @Autowired
    private IProductService productService;

    // ================= ADD PRODUCT =================
    @PostMapping("/add")
    public void addProduct(@ModelAttribute ProductRequest request,
                           HttpServletResponse response) throws IOException {

        productService.addProduct(request);

        // redirect after adding product
        response.sendRedirect("/home");
    }

    // ================= VIEW ALL PRODUCTS =================
    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // ================= PRODUCTS BY CATEGORY =================
    @GetMapping("/category/{id}")
    public List<Product> getProductsByCategory(@PathVariable Long id) {
        return productService.getProductsByCategory(id);
    }

    // ================= DELETE PRODUCT =================
    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}