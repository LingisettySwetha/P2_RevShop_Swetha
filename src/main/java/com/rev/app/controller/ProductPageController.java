package com.rev.app.controller;

import com.rev.app.entity.Product;
import com.rev.app.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProductPageController {

    @Autowired
    private IProductService productService;

    // Open add product page
    @GetMapping("/add-product")
    public String showAddProductPage() {
        return "add-product";
    }

    // Show all products (Buyer view)
    @GetMapping("/products")
    public String showProducts(Model model) {

        List<Product> products = productService.getAllProducts();

        model.addAttribute("products", products);

        return "products";
    }
}