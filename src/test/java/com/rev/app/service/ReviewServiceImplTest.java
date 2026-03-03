package com.rev.app.service;

import com.rev.app.entity.Product;
import com.rev.app.entity.Review;
import com.rev.app.entity.User;
import com.rev.app.exception.InvalidRequestException;
import com.rev.app.repository.IProductRepository;
import com.rev.app.repository.IReviewRepository;
import com.rev.app.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private IReviewRepository reviewRepository;
    @Mock
    private IUserRepository userRepository;
    @Mock
    private IProductRepository productRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    void testAddReview_InvalidRating() {
        assertThrows(InvalidRequestException.class, () -> 
            reviewService.addReview(1L, 1L, 6, "Bad")
        );
    }

    @Test
    void testAddReview_AlreadyReviewed() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(productRepository.findById(1L)).thenReturn(Optional.of(new Product()));
        when(reviewRepository.findByUser_UserIdAndProduct_ProductId(1L, 1L)).thenReturn(Optional.of(new Review()));

        assertThrows(InvalidRequestException.class, () -> 
            reviewService.addReview(1L, 1L, 5, "Great")
        );
    }
}
