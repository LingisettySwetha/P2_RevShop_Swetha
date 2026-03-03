package com.rev.app.service;

import com.rev.app.entity.User;
import com.rev.app.exception.ResourceNotFoundException;
import com.rev.app.repository.IUserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPasswordHash("plainPassword");
        testUser.setPhone("1234567890");
        testUser.setRole("BUYER");
    }

    @Test
    void testRegisterUser_HashesPassword() {
        when(passwordEncoder.encode("plainPassword"))
                .thenReturn("$2a$10$hashedPassword");
        when(userRepository.save(any(User.class)))
                .thenReturn(testUser);

        User result = userService.registerUser(testUser);

        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(testUser);
        assertNotNull(result);
    }

    @Test
    void testRegisterUser_SetsDefaultRole() {
        testUser.setRole(null);
        when(passwordEncoder.encode(anyString()))
                .thenReturn("$2a$10$hashedPassword");
        when(userRepository.save(any(User.class)))
                .thenReturn(testUser);

        userService.registerUser(testUser);

        assertEquals("USER", testUser.getRole());
    }

    @Test
    void testLoginUser_SuccessWithCorrectCredentials() {
        testUser.setPasswordHash("$2a$10$hashedPassword");
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("correctPassword",
                "$2a$10$hashedPassword"))
                .thenReturn(true);

        User result = userService.loginUser(
                "test@example.com", "correctPassword");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testLoginUser_ThrowsOnWrongPassword() {
        testUser.setPasswordHash("$2a$10$hashedPassword");
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongPassword",
                "$2a$10$hashedPassword"))
                .thenReturn(false);

        assertThrows(BadCredentialsException.class, () ->
                userService.loginUser(
                        "test@example.com", "wrongPassword"));
    }

    @Test
    void testLoginUser_ThrowsOnNonExistentUser() {
        when(userRepository.findByEmail("nobody@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                userService.loginUser(
                        "nobody@example.com", "password"));
    }
}
