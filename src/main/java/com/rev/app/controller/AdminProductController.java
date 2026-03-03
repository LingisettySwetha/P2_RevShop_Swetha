package com.rev.app.controller;

import com.rev.app.entity.Product;
import com.rev.app.exception.UnauthorizedException;
import com.rev.app.repository.IUserRepository;
import com.rev.app.service.ICategoryService;
import com.rev.app.service.IImageStorageService;
import com.rev.app.service.IProductService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IImageStorageService imageStorageService;

    private void checkAdmin(HttpSession session){
        String role = (String) session.getAttribute("role");

        if (role == null || !role.equals("ADMIN"))
            throw new UnauthorizedException("Admin access only");
    }

    @GetMapping
    public String listProducts(HttpSession session, Model model) {
        log.info("Admin: Listing products");

        checkAdmin(session);

        model.addAttribute("products",
                productService.getAllProducts());

        return "admin-products";
    }

    @GetMapping("/add")
    public String addProductPage(HttpSession session,
                                 Model model) {

        checkAdmin(session);

        model.addAttribute("product", new Product());
        model.addAttribute("sellers", userRepository.findAll());
        model.addAttribute("categories", categoryService.getAllCategories());

        return "add-product";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              HttpSession session) {

        checkAdmin(session);

        if (imageFile != null && !imageFile.isEmpty()) {
            product.setImageUrl(imageStorageService.storeProductImage(imageFile));
        } else if (product.getProductId() != null) {
            Product existing = productService.getProductById(product.getProductId());
            product.setImageUrl(existing.getImageUrl());
        }

        productService.saveProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id,
                              HttpSession session,
                              Model model) {

        checkAdmin(session);

        model.addAttribute("product",
                productService.getProductById(id));
        model.addAttribute("sellers",
                userRepository.findAll());
        model.addAttribute("categories",
                categoryService.getAllCategories());

        return "edit-product";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id,
                                HttpSession session) {

        checkAdmin(session);

        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}
