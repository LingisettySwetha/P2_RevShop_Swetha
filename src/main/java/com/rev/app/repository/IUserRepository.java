package com.rev.app.repository;

import com.rev.app.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {

    User saveUser(User user);

    Optional<User> findUserById(Long id);

    Optional<User> findUserByEmail(String email);

    List<User> findAllUsers();

    void deleteUser(Long id);
    
    Optional<User> findByEmail(String email);
}