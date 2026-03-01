package com.rev.app.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void testGenerateToken_ReturnsNonNull() {
        String token = jwtUtil.generateToken("test@example.com", "USER");
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername_ReturnsCorrectEmail() {
        String email = "test@example.com";
        String token = jwtUtil.generateToken(email, "USER");

        String extracted = jwtUtil.extractUsername(token);
        assertEquals(email, extracted);
    }

    @Test
    void testExtractRole_ReturnsCorrectRole() {
        String token = jwtUtil.generateToken("test@example.com", "ADMIN");

        String role = jwtUtil.extractRole(token);
        assertEquals("ADMIN", role);
    }

    @Test
    void testValidateToken_ReturnsTrueForValidToken() {
        String token = jwtUtil.generateToken("test@example.com", "USER");

        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void testValidateToken_ReturnsFalseForTamperedToken() {
        String token = jwtUtil.generateToken("test@example.com", "USER");
        String tampered = token + "xyz";

        assertFalse(jwtUtil.validateToken(tampered));
    }

    @Test
    void testValidateToken_ReturnsFalseForEmptyToken() {
        assertFalse(jwtUtil.validateToken(""));
    }

    @Test
    void testValidateToken_ReturnsFalseForNull() {
        assertFalse(jwtUtil.validateToken(null));
    }

    @Test
    void testGenerateToken_DifferentEmailsProduceDifferentTokens() {
        String token1 = jwtUtil.generateToken("a@test.com", "USER");
        String token2 = jwtUtil.generateToken("b@test.com", "USER");

        assertNotEquals(token1, token2);
    }
}
