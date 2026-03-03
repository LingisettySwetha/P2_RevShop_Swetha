package com.rev.app.controller;

import com.rev.app.entity.User;
import com.rev.app.service.ICategoryService;
import com.rev.app.service.IProductService;
import com.rev.app.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class PageController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    
    @GetMapping({"/", "/home"})
    public String home(Model model, HttpSession session) {
        
        String role = (String) session.getAttribute("role");
        if ("SELLER".equals(role)) {
            log.info("Seller attempted to access home page – redirecting to seller dashboard");
            return "redirect:/seller/dashboard";
        }
        log.info("Accessing home page");
        model.addAttribute("featuredProducts", productService.getAllProducts().stream().limit(8).toList());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "home";
    }

    
    @GetMapping("/login")
    public String loginPage() {
        return "Login";
    }

    
    
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "Register";
    }

    
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    
    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "profile";
    }

    
    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String name,
                                @RequestParam String email,
                                @RequestParam(required = false) String phone,
                                @RequestParam(required = false) String currentPassword,
                                @RequestParam(required = false) String newPassword,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        User user = userService.getUserById(userId);

        
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);

        
        if (newPassword != null && !newPassword.isBlank()) {
            if (currentPassword == null || !passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
                redirectAttributes.addFlashAttribute("error", "Current password is incorrect");
                return "redirect:/profile";
            }
            user.setPasswordHash(passwordEncoder.encode(newPassword));
        }

        userService.updateUser(user);

        
        session.setAttribute("username", name);

        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        log.info("Profile updated for user ID: {}", userId);
        return "redirect:/profile";
    }
}