package com.rev.app.controller;

import com.rev.app.entity.Product;
import com.rev.app.repository.ICategoryRepository;
import com.rev.app.repository.IUserRepository;
import com.rev.app.service.IProductService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ICategoryRepository categoryRepository;


 
    @GetMapping
    public String listProducts(HttpSession session, Model model) {

        String role = (String) session.getAttribute("role");

       
        if (role == null || !role.equals("ADMIN")) {
            return "redirect:/login";
        }

        model.addAttribute("products", productService.getAllProducts());
        return "admin-products";
    }


 
    @GetMapping("/add")
    public String addProductPage(HttpSession session, Model model) {

        String role = (String) session.getAttribute("role");

        if (role == null || !role.equals("ADMIN")) {
            return "redirect:/login";
        }

        model.addAttribute("product", new Product());
        model.addAttribute("sellers", userRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());

        return "add-product";
    }


  
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product,
                              HttpSession session) {

        String role = (String) session.getAttribute("role");

        if (role == null || !role.equals("ADMIN")) {
            return "redirect:/login";
        }

        productService.saveProduct(product);
        return "redirect:/admin/products";
    }


   
    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id,
                              HttpSession session,
                              Model model) {

        String role = (String) session.getAttribute("role");

        if (role == null || !role.equals("ADMIN")) {
            return "redirect:/login";
        }

        model.addAttribute("product",
                productService.getProductById(id));

        model.addAttribute("sellers",
                userRepository.findAll());

        model.addAttribute("categories",
                categoryRepository.findAll());

        return "edit-product";
    }


   
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id,
                                HttpSession session) {

        String role = (String) session.getAttribute("role");

        if (role == null || !role.equals("ADMIN")) {
            return "redirect:/login";
        }

        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}