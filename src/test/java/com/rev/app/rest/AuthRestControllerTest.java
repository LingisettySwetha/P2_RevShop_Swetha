package com.rev.app.rest;

import com.rev.app.entity.User;
import com.rev.app.security.JwtUtil;
import com.rev.app.service.ICartService;
import com.rev.app.service.IUserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthRestControllerTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private IUserService userService;
    @Mock
    private ICartService cartService;

    @Mock
    private HttpServletRequest httpRequest;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AuthRestController authRestController;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPasswordHash("$2a$10$hashed");
        testUser.setRole("BUYER");
    }

    @Test
    void testLogin_Success() {
        when(userService.loginUser("test@example.com", "password"))
                .thenReturn(testUser);
        when(jwtUtil.generateToken("test@example.com", "BUYER"))
                .thenReturn("mock-jwt-token");
        when(httpRequest.getSession()).thenReturn(session);
        when(cartService.viewCart(1L)).thenReturn(java.util.Collections.emptyList());

        Map<String, String> request = new HashMap<>();
        request.put("email", "test@example.com");
        request.put("password", "password");

        ResponseEntity<Map<String, Object>> response =
                authRestController.login(request, httpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("mock-jwt-token",
                response.getBody().get("token"));
        assertEquals("BUYER",
                response.getBody().get("role"));
        assertEquals("Test User",
                response.getBody().get("name"));
    }

    @Test
    void testLogin_Failure() {
        when(userService.loginUser("test@example.com", "wrong"))
                .thenThrow(new BadCredentialsException("Invalid"));

        Map<String, String> request = new HashMap<>();
        request.put("email", "test@example.com");
        request.put("password", "wrong");

        ResponseEntity<Map<String, Object>> response =
                authRestController.login(request, httpRequest);

        assertEquals(HttpStatus.UNAUTHORIZED,
                response.getStatusCode());
        assertEquals("Invalid email or password",
                response.getBody().get("error"));
    }
}
