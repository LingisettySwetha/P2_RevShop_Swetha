package com.rev.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rev.app.dto.ReviewDTO;
import com.rev.app.entity.Product;
import com.rev.app.entity.Review;
import com.rev.app.entity.User;
import com.rev.app.exception.InvalidRequestException;
import com.rev.app.exception.ResourceNotFoundException;
import com.rev.app.mapper.ReviewMapper;
import com.rev.app.repository.IProductRepository;
import com.rev.app.repository.IReviewRepository;
import com.rev.app.repository.IUserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReviewServiceImpl
        implements IReviewService {

    @Autowired
    private IReviewRepository reviewRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IProductRepository productRepository;

    
    @Override
    public void addReview(Long userId,
                          Long productId,
                          Integer rating,
                          String comment) {
        log.info("Adding review for product {} by user {}", productId, userId);

        if (rating < 1 || rating > 5)
            throw new InvalidRequestException(
                    "Rating must be 1 to 5");

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found"));

      
        if (reviewRepository
                .findByUser_UserIdAndProduct_ProductId(
                        userId, productId)
                .isPresent()) {

            throw new InvalidRequestException(
                    "You already reviewed this product");
        }

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(rating);
        review.setComment(comment);

        reviewRepository.save(review);
    }

    
    @Override
    public List<ReviewDTO>
    getProductReviews(Long productId) {

        return reviewRepository
                .findByProduct_ProductId(productId)
                .stream()
                .map(ReviewMapper::toDTO)
                .toList();
    }

    
    @Override
    public void updateReview(Long reviewId,
                             Integer rating,
                             String comment) {

        if (rating < 1 || rating > 5)
            throw new InvalidRequestException(
                    "Rating must be 1 to 5");

        Review review =
                reviewRepository.findById(reviewId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Review not found"));

        review.setRating(rating);
        review.setComment(comment);

        reviewRepository.save(review);
    }

   
    @Override
    public void deleteReview(Long reviewId) {

        if (!reviewRepository.existsById(reviewId))
            throw new ResourceNotFoundException(
                    "Review not found");

        reviewRepository.deleteById(reviewId);
    }
}