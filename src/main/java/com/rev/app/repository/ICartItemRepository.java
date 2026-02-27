package com.rev.app.repository;

import com.rev.app.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ICartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCartCartId(Long cartId);

    Optional<CartItem> findByCartCartIdAndProductProductId(
            Long cartId, Long productId);
}