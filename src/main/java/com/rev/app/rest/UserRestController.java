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

import com.rev.app.dto.RegisterRequest;
import com.rev.app.entity.User;
import com.rev.app.mapper.UserMapper;
import com.rev.app.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserRestController {

    @Autowired
    private IUserService userService;

    
    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegisterRequest request) {

        User user = UserMapper.toEntity(request);
        userService.registerUser(user);

        return "User Registered Successfully";
    }

    
    @PostMapping("/api/register")
    public User registerUserApi(@RequestBody RegisterRequest request) {

        User user = UserMapper.toEntity(request);
        return userService.registerUser(user);
    }
}