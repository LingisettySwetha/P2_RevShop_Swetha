package com.rev.app.repository;

import com.rev.app.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICartRepository
        extends JpaRepository<Cart, Long> {

    Cart findByUser_UserId(Long userId);
}