//package com.rev.app.rest;
//
//import com.rev.app.dto.RegisterRequest;
//import com.rev.app.entity.User;
//import com.rev.app.mapper.UserMapper;
//import com.rev.app.service.IUserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/users")
//public class UserRestController {
//
//    @Autowired
//    private IUserService userService;
//
//    @PostMapping("/register")
//    public User registerUser(@RequestBody RegisterRequest request) {
//
//        
//        User user = UserMapper.toEntity(request);
//
//        
//        return userService.registerUser(user);
//    }
//}
package com.rev.app.rest;

import com.rev.app.dto.LoginRequest;
import com.rev.app.dto.RegisterRequest;
import com.rev.app.entity.User;
import com.rev.app.mapper.UserMapper;
import com.rev.app.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
@RestController
@RequestMapping("/users")
public class UserRestController {

    @Autowired
    private IUserService userService;

    
    @PostMapping("/register")
    public void registerUser(
            @ModelAttribute RegisterRequest request,
            HttpServletResponse response) throws IOException {

        User user = UserMapper.toEntity(request);
        userService.registerUser(user);

        response.sendRedirect("/login");
    }
    
    @PostMapping("/api/register")
    public User registerUserApi(@RequestBody RegisterRequest request) {

        User user = UserMapper.toEntity(request);
        return userService.registerUser(user);
    }
    
//    @PostMapping("/login")
//    public String loginUser(@ModelAttribute LoginRequest request) {
//
//        userService.loginUser(
//                request.getEmail(),
//                request.getPassword()
//        );
//
//        return "Login Successful ✅";
//    }
    
    @PostMapping("/login")
    public void loginUser(
            @ModelAttribute LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse response) throws IOException {

        try {

            User user = userService.loginUser(
                    request.getEmail(),
                    request.getPassword());

            HttpSession session = httpRequest.getSession();
            session.setAttribute("loggedUser", user);

            response.sendRedirect("/home");

        } catch (Exception e) {

            // redirect back to login with error message
            response.sendRedirect("/login?error=true");
        }
    }
}