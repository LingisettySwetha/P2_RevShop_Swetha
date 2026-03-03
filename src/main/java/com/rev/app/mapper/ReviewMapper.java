package com.rev.app.mapper;

import com.rev.app.dto.ReviewDTO;
import com.rev.app.entity.Review;

public class ReviewMapper {

    public static ReviewDTO toDTO(Review review) {

        ReviewDTO dto = new ReviewDTO();

        dto.setReviewId(review.getReviewId());
        dto.setUsername(review.getUser().getName());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());

        return dto;
    }
}