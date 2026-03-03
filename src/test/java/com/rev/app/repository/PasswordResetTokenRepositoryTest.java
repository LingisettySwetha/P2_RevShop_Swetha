package com.rev.app.repository;

import com.rev.app.entity.PasswordResetToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordResetTokenRepositoryTest {

    @Mock
    private IPasswordResetTokenRepository passwordResetTokenRepository;

    @Test
    void testFindByToken() {
        PasswordResetToken token = new PasswordResetToken();
        token.setToken("token123");

        when(passwordResetTokenRepository.findByToken("token123"))
                .thenReturn(Optional.of(token));

        Optional<PasswordResetToken> found = passwordResetTokenRepository.findByToken("token123");
        assertTrue(found.isPresent());
    }
}
