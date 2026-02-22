package com.rev.app.service;

import com.rev.app.entity.User;
import com.rev.app.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public User registerUser(User user) {
        return userRepository.saveUser(user);
    }
}