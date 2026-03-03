package com.rev.app.service;

import com.rev.app.entity.PasswordResetToken;
import com.rev.app.entity.User;
import com.rev.app.exception.InvalidRequestException;
import com.rev.app.repository.IPasswordResetTokenRepository;
import com.rev.app.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordRecoveryServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IPasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordRecoveryServiceImpl passwordRecoveryService;

    @Test
    void testCreateResetToken_UserExists() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        String token = passwordRecoveryService.createResetToken("test@example.com");

        assertNotNull(token);
        verify(passwordResetTokenRepository).save(any(PasswordResetToken.class));
    }

    @Test
    void testTokenValidation_Valid() {
        PasswordResetToken token = new PasswordResetToken();
        token.setToken("abc");
        token.setUsed(false);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        when(passwordResetTokenRepository.findByToken("abc"))
                .thenReturn(Optional.of(token));

        assertTrue(passwordRecoveryService.isTokenValid("abc"));
    }

    @Test
    void testResetPassword_InvalidToken() {
        when(passwordResetTokenRepository.findByToken("bad"))
                .thenReturn(Optional.empty());
        assertThrows(InvalidRequestException.class,
                () -> passwordRecoveryService.resetPassword("bad", "newpass"));
    }
}
