package com.rev.app.rest;

import com.rev.app.dto.RegisterRequest;
import com.rev.app.entity.User;
import com.rev.app.mapper.UserMapper;
import com.rev.app.service.ISellerService;
import com.rev.app.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserRestController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ISellerService sellerService;

    
    @PostMapping("/register")
    public void registerUser(
            @ModelAttribute RegisterRequest request,
            HttpServletResponse response) throws IOException {
        log.info("REST: Registering user (web form): {}", request.getEmail());

        User user = UserMapper.toEntity(request);
        User saved = userService.registerUser(user);
        if ("SELLER".equals(saved.getRole())) {
            sellerService.createSeller(saved, saved.getName() + " Store");
        }

        response.sendRedirect("/login");
    }
    
    @PostMapping("/api/register")
    public User registerUserApi(@RequestBody RegisterRequest request) {

        User user = UserMapper.toEntity(request);
        User saved = userService.registerUser(user);
        if ("SELLER".equals(saved.getRole())) {
            sellerService.createSeller(saved, saved.getName() + " Store");
        }
        return saved;
    }
}
