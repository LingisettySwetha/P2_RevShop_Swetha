package com.rev.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rev.app.entity.User;

import com.rev.app.exception.ResourceNotFoundException;
import com.rev.app.exception.UserAlreadyExistsException;
import com.rev.app.repository.IUserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

   
    @Override
    public User registerUser(User user) {
        log.info("Registering user with email: {}", user.getEmail());
        
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            log.warn("Registration failed: Email {} already exists", user.getEmail());
            throw new UserAlreadyExistsException("Email already exists: " + user.getEmail());
        }

        
        user.setPasswordHash(
                passwordEncoder.encode(user.getPasswordHash()));

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getUserId());
        return savedUser;
    }

   
    @Override
    public User loginUser(String email, String password) {

       
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        
        if (!passwordEncoder.matches(password,
                user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid password");
        }

        return user;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public User updateUser(User user) {
        log.info("Updating user with ID: {}", user.getUserId());
        return userRepository.save(user);
    }
}