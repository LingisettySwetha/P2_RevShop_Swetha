package com.rev.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rev.app.entity.Cart;

public interface ICartRepository extends JpaRepository<Cart, Long> {

    Cart findByUserUserId(Long userId);

}