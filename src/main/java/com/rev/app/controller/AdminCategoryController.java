package com.rev.app.controller;

import com.rev.app.entity.Category;
import com.rev.app.exception.UnauthorizedException;
import com.rev.app.service.ICategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    @Autowired
    private ICategoryService categoryService;

    private void checkAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role == null || !role.equals("ADMIN")) {
            throw new UnauthorizedException("Admin access only");
        }
    }

    @GetMapping
    public String listCategories(HttpSession session, Model model) {
        log.info("Admin: Listing all categories");
        checkAdmin(session);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin-categories";
    }

    @GetMapping("/add")
    public String addCategoryPage(HttpSession session, Model model) {
        log.info("Admin: Accessing add category page");
        checkAdmin(session);
        model.addAttribute("category", new Category());
        return "admin-add-category";
    }

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute Category category, HttpSession session) {
        log.info("Admin: Saving category {}", category.getCategoryName());
        checkAdmin(session);
        categoryService.saveCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String editCategoryPage(@PathVariable Long id, HttpSession session, Model model) {
        log.info("Admin: Accessing edit category page for category {}", id);
        checkAdmin(session);
        model.addAttribute("category", categoryService.getCategoryById(id));
        return "admin-edit-category";
    }

    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable Long id, @ModelAttribute Category category, HttpSession session) {
        log.info("Admin: Updating category {}", id);
        checkAdmin(session);
        categoryService.updateCategory(id, category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, HttpSession session) {
        log.info("Admin: Deleting category {}", id);
        checkAdmin(session);
        categoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }
}
