package com.rev.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rev.app.entity.User;

public interface IUserRepository extends JpaRepository<User, Long> {

   
    Optional<User> findByEmailAndPasswordHash(String email,
                                              String passwordHash);

    
    Optional<User> findByEmail(String email);

    long count();
}