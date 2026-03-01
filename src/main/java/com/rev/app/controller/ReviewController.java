package com.rev.app.controller;

import com.rev.app.exception.UnauthorizedException;
import com.rev.app.service.IReviewService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private IReviewService reviewService;

    
    @PostMapping("/add")
    public String addReview(@RequestParam Long productId,
                            @RequestParam Integer rating,
                            @RequestParam String comment,
                            HttpSession session) {

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null)
            throw new UnauthorizedException("Login required");

        reviewService.addReview(
                userId,
                productId,
                rating,
                comment);

        return "redirect:/products";
    }

   
    @PostMapping("/update")
    public String updateReview(@RequestParam Long reviewId,
                               @RequestParam Integer rating,
                               @RequestParam String comment,
                               HttpSession session) {

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null)
            throw new UnauthorizedException("Login required");

        reviewService.updateReview(
                reviewId,
                rating,
                comment);

        return "redirect:/products";
    }

   
    @GetMapping("/delete/{reviewId}")
    public String deleteReview(@PathVariable Long reviewId,
                               HttpSession session) {

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null)
            throw new UnauthorizedException("Login required");

        reviewService.deleteReview(reviewId);

        return "redirect:/products";
    }
}