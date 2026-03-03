package com.rev.app.controller;

import com.rev.app.exception.UnauthorizedException;
import com.rev.app.exception.InvalidRequestException;
import com.rev.app.service.IReviewService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private IReviewService reviewService;

    
    @PostMapping("/add")
    public String addReview(@RequestParam Long productId,
                            @RequestParam Integer rating,
                            @RequestParam String comment,
                            RedirectAttributes redirectAttributes,
                            HttpSession session) {
        log.info("UI: Adding review for product {}", productId);

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null)
            throw new UnauthorizedException("Login required");

        try {
            reviewService.addReview(
                    userId,
                    productId,
                    rating,
                    comment);
            redirectAttributes.addFlashAttribute("success",
                    "Review submitted successfully");
        } catch (InvalidRequestException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/products/" + productId;
    }

   
    @PostMapping("/update")
    public String updateReview(@RequestParam Long reviewId,
                               @RequestParam Long productId,
                               @RequestParam Integer rating,
                               @RequestParam String comment,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null)
            throw new UnauthorizedException("Login required");

        reviewService.updateReview(
                reviewId,
                rating,
                comment);

        redirectAttributes.addFlashAttribute("success",
                "Review updated successfully");
        return "redirect:/products/" + productId;
    }

   
    @GetMapping("/delete/{reviewId}")
    public String deleteReview(@PathVariable Long reviewId,
                               @RequestParam Long productId,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null)
            throw new UnauthorizedException("Login required");

        reviewService.deleteReview(reviewId);

        redirectAttributes.addFlashAttribute("success",
                "Review deleted successfully");
        return "redirect:/products/" + productId;
    }
}
