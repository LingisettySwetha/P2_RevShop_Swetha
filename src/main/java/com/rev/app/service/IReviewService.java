package com.rev.app.service;

import java.util.List;

import com.rev.app.dto.ReviewDTO;

public interface IReviewService {

    void addReview(Long userId,
                   Long productId,
                   Integer rating,
                   String comment);

    List<ReviewDTO>
    getProductReviews(Long productId);

    void updateReview(Long reviewId,
                      Integer rating,
                      String comment);

    void deleteReview(Long reviewId);
}