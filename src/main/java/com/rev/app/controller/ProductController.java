package com.rev.app.controller;

import com.rev.app.service.IProductService;
import com.rev.app.service.ICategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/products")
    public String showProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long category,
            Model model) {
        log.info("Viewing product list (keyword: {}, category: {})", keyword, category);

        model.addAttribute("products",
                productService.searchProducts(keyword, category));

        model.addAttribute("categories",
                categoryService.getAllCategories());

        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);

        return "products";
    }

    @GetMapping("/products/{id}")
    public String viewProductDetails(@PathVariable Long id, Model model) {
        log.info("Viewing details for product ID: {}", id);
        model.addAttribute("product", productService.getProductById(id));
        return "product-details";
    }
}