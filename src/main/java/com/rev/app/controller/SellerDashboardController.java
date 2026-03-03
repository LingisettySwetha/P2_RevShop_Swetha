package com.rev.app.controller;

import com.rev.app.entity.Product;
import com.rev.app.entity.OrderItem;
import com.rev.app.entity.Seller;
import com.rev.app.entity.User;
import com.rev.app.repository.ISellerRepository;
import com.rev.app.repository.IUserRepository;
import com.rev.app.service.ICategoryService;
import com.rev.app.service.IProductService;
import com.rev.app.service.ISellerService;
import com.rev.app.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/seller")
public class SellerDashboardController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ISellerService sellerService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ISellerRepository sellerRepository;

    @Autowired
    private IUserRepository userRepository;

    private Long getUserId(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            log.warn("Unauthorized seller dashboard access attempt");
        }
        return userId;
    }

    private Seller resolveSellerProfile(Long userId) {
        return sellerRepository.findByUserUserId(userId).orElseGet(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
            try {
                sellerService.createSeller(user, user.getName() + " Store");
            } catch (DataIntegrityViolationException ex) {
                log.warn("Seller profile creation raced for user {}. Retrying fetch.", userId);
            }
            return sellerRepository.findByUserUserId(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Seller profile not found for user: " + userId));
        });
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Long userId = getUserId(session);
        if (userId == null) return "redirect:/login";

        List<OrderItem> sellerOrders = sellerService.getSellerOrders(userId);
        long newOrderCount = sellerOrders.stream()
                .filter(item -> "PLACED".equals(item.getOrder().getOrderStatus()))
                .map(item -> item.getOrder().getOrderId())
                .distinct()
                .count();
        long pendingOrderCount = sellerOrders.stream()
                .filter(item -> "PLACED".equals(item.getOrder().getOrderStatus())
                        || "PROCESSING".equals(item.getOrder().getOrderStatus()))
                .map(item -> item.getOrder().getOrderId())
                .distinct()
                .count();

        model.addAttribute("totalRevenue", sellerService.calculateTotalRevenue(userId));
        model.addAttribute("products", productService.getProductsBySeller(userId));
        model.addAttribute("orders", sellerOrders);
        model.addAttribute("newOrderCount", newOrderCount);
        model.addAttribute("pendingOrderCount", pendingOrderCount);
        model.addAttribute("lowStockProducts", 
            productService.getProductsBySeller(userId).stream()
                .filter(p -> p.getQuantity() < 5)
                .toList()
        );

        return "seller-dashboard";
    }

    @GetMapping("/products")
    public String products(HttpSession session, Model model) {
        Long userId = getUserId(session);
        if (userId == null) return "redirect:/login";

        model.addAttribute("products", productService.getProductsBySeller(userId));
        return "seller-products";
    }

    @GetMapping("/products/add")
    public String addProductPage(HttpSession session, Model model) {
        Long userId = getUserId(session);
        if (userId == null) return "redirect:/login";

        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "seller-add-product";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute Product product, HttpSession session) {
        Long userId = getUserId(session);
        if (userId == null) return "redirect:/login";

        product.setSeller(resolveSellerProfile(userId));

        
        if (product.getCategory() != null && product.getCategory().getCategoryId() != null) {
            product.setCategory(categoryService.getCategoryById(product.getCategory().getCategoryId()));
        }

        productService.saveProduct(product);
        return "redirect:/seller/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id, HttpSession session, Model model) {
        Long userId = getUserId(session);
        if (userId == null) return "redirect:/login";

        Product product = productService.getProductById(id);
        if (!product.getSeller().getUser().getUserId().equals(userId)) {
             return "redirect:/seller/products";
        }

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "seller-edit-product";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, HttpSession session) {
        Long userId = getUserId(session);
        if (userId == null) return "redirect:/login";

        Product product = productService.getProductById(id);
         if (product.getSeller().getUser().getUserId().equals(userId)) {
            productService.deleteProduct(id);
        }

        return "redirect:/seller/products";
    }

    @GetMapping("/orders")
    public String orders(HttpSession session, Model model) {
        Long userId = getUserId(session);
        if (userId == null) return "redirect:/login";

        List<OrderItem> sellerOrders = sellerService.getSellerOrders(userId);
        long newOrderCount = sellerOrders.stream()
                .filter(item -> "PLACED".equals(item.getOrder().getOrderStatus()))
                .map(item -> item.getOrder().getOrderId())
                .distinct()
                .count();

        model.addAttribute("orders", sellerOrders);
        model.addAttribute("newOrderCount", newOrderCount);
        return "seller-orders";
    }

    @GetMapping("/notifications/new-orders")
    @ResponseBody
    public Map<String, Object> newOrderNotifications(HttpSession session) {
        Long userId = getUserId(session);
        Map<String, Object> response = new HashMap<>();

        if (userId == null) {
            response.put("newOrderCount", 0);
            response.put("pendingOrderCount", 0);
            return response;
        }

        List<OrderItem> sellerOrders = sellerService.getSellerOrders(userId);
        long newOrderCount = sellerOrders.stream()
                .filter(item -> "PLACED".equals(item.getOrder().getOrderStatus()))
                .map(item -> item.getOrder().getOrderId())
                .distinct()
                .count();
        long pendingOrderCount = sellerOrders.stream()
                .filter(item -> "PLACED".equals(item.getOrder().getOrderStatus())
                        || "PROCESSING".equals(item.getOrder().getOrderStatus()))
                .map(item -> item.getOrder().getOrderId())
                .distinct()
                .count();

        response.put("newOrderCount", newOrderCount);
        response.put("pendingOrderCount", pendingOrderCount);
        return response;
    }
    @PostMapping("/orders/ship/{id}")
    public String shipOrder(@PathVariable Long id, HttpSession session) {
        Long userId = getUserId(session);
        if (userId == null) return "redirect:/login";

        sellerService.shipOrderItem(id);
        return "redirect:/seller/orders";
    }

    @PostMapping("/orders/update-status")
    public String updateOrderStatus(@RequestParam Long orderId, @RequestParam String status) {
        sellerService.updateOrderStatus(orderId, status);
        return "redirect:/seller/orders";
    }

    @PostMapping("/orders/update-payment")
    public String updatePaymentStatus(@RequestParam Long orderId, @RequestParam String status) {
        sellerService.updatePaymentStatus(orderId, status);
        return "redirect:/seller/orders";
    }
}
