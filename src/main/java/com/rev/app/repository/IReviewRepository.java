package com.rev.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rev.app.entity.Review;

public interface IReviewRepository
        extends JpaRepository<Review, Long> {

    List<Review> findByProduct_ProductId(Long productId);

    Optional<Review>
    findByUser_UserIdAndProduct_ProductId(
            Long userId, Long productId);
}