package com.rev.app.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.rev.app.dto.ReviewDTO;
import com.rev.app.exception.UnauthorizedException;
import com.rev.app.service.IReviewService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/reviews")
public class ReviewRestController {

    @Autowired
    private IReviewService reviewService;

    
    @PostMapping
    public String addReview(@RequestParam Long productId,
                            @RequestParam Integer rating,
                            @RequestParam String comment,
                            HttpSession session){

        Long userId =
                (Long) session.getAttribute("userId");

        if(userId == null)
            throw new UnauthorizedException("Login required");

        reviewService.addReview(userId,
                productId, rating, comment);

        return "Review added";
    }

  
    @GetMapping("/{productId}")
    public List<ReviewDTO>
    getReviews(@PathVariable Long productId){

        return reviewService
                .getProductReviews(productId);
    }

    
    @PutMapping("/{reviewId}")
    public String updateReview(
            @PathVariable Long reviewId,
            @RequestParam Integer rating,
            @RequestParam String comment,
            HttpSession session){

        Long userId =
                (Long) session.getAttribute("userId");

        if(userId == null)
            throw new UnauthorizedException("Login required");

        reviewService.updateReview(
                reviewId, rating, comment);

        return "Review updated";
    }

  
    @DeleteMapping("/{reviewId}")
    public String deleteReview(
            @PathVariable Long reviewId,
            HttpSession session){

        Long userId =
                (Long) session.getAttribute("userId");

        if(userId == null)
            throw new UnauthorizedException("Login required");

        reviewService.deleteReview(reviewId);

        return "Review deleted";
    }
}