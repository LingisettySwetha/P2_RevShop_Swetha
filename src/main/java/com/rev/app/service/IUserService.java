package com.rev.app.service;

import com.rev.app.entity.User;

public interface IUserService {

    User registerUser(User user);
    
    User loginUser(String email, String password);
    
    User getUserById(Long id);
    
    User updateUser(User user);
}