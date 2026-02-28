package com.rev.app.controller;

import com.rev.app.service.IProductService;
import com.rev.app.repository.ICategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryRepository categoryRepository;

    @GetMapping("/products")
    public String showProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long category,
            Model model) {

        model.addAttribute("products",
                productService.searchProducts(keyword, category));

        model.addAttribute("categories",
                categoryRepository.findAll());

        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);

        return "products";
    }
}