package com.rev.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rev.app.entity.Wishlist;

public interface IWishlistRepository
        extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByUser_UserId(Long userId);

    Optional<Wishlist> findByUser_UserIdAndProduct_ProductId(
            Long userId,
            Long productId
    );
}