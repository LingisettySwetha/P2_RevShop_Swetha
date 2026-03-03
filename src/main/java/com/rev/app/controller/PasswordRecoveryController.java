package com.rev.app.controller;

import com.rev.app.exception.InvalidRequestException;
import com.rev.app.service.IPasswordRecoveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
public class PasswordRecoveryController {

    @Autowired
    private IPasswordRecoveryService passwordRecoveryService;

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String requestPasswordReset(@RequestParam String email,
                                       RedirectAttributes redirectAttributes) {
        String token = passwordRecoveryService.createResetToken(email);
        redirectAttributes.addFlashAttribute("success",
                "If an account exists for this email, a reset link has been generated.");

        if (token != null) {
            redirectAttributes.addFlashAttribute("devResetLink", "/reset-password?token=" + token);
            log.info("Password reset link generated for {}", email);
        }

        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam(required = false) String token,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        if (!passwordRecoveryService.isTokenValid(token)) {
            redirectAttributes.addFlashAttribute("error", "Invalid or expired reset link");
            return "redirect:/forgot-password";
        }

        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            return "redirect:/reset-password?token=" + token;
        }

        try {
            passwordRecoveryService.resetPassword(token, newPassword);
            redirectAttributes.addFlashAttribute("success",
                    "Password reset successful. Please login.");
            return "redirect:/login";
        } catch (InvalidRequestException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/forgot-password";
        }
    }
}
