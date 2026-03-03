package com.rev.app.service;

public interface IPasswordRecoveryService {

    String createResetToken(String email);

    boolean isTokenValid(String token);

    void resetPassword(String token, String newPassword);
}
