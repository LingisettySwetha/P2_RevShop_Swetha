package com.rev.app.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rev.app.entity.User;
import com.rev.app.security.JwtUtil;
import com.rev.app.service.IUserService;

import com.rev.app.service.ICartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private IUserService userService;

    @Autowired
    private ICartService cartService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {

        try {
            String email = request.get("email");
            String password = request.get("password");

            User user = userService.loginUser(email, password);

            String token = jwtUtil.generateToken(
                    user.getEmail(), user.getRole());

            
            HttpSession session = httpRequest.getSession();
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("username", user.getName());
            session.setAttribute("role", user.getRole());

            
            int count = cartService.viewCart(user.getUserId()).size();
            session.setAttribute("cartCount", count);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("role", user.getRole());
            response.put("name", user.getName());
            response.put("userId", user.getUserId());

            log.info("User {} logged in successfully", email);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Login failed for user {}: {}", request.get("email"), e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid email or password");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(error);
        }
    }
}
