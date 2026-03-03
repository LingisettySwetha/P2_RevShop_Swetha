package com.rev.app.rest;

import com.rev.app.exception.UnauthorizedException;
import com.rev.app.service.ICartService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartRestControllerTest {

    @Mock
    private ICartService cartService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private CartRestController cartRestController;

    @Test
    void testAddToCart_Success() {
        when(session.getAttribute("userId")).thenReturn(1L);
        String result = cartRestController.addToCart(101L, session);
        assertEquals("Added to cart", result);
        verify(cartService).addToCart(1L, 101L);
    }

    @Test
    void testAddToCart_Unauthorized() {
        when(session.getAttribute("userId")).thenReturn(null);
        assertThrows(UnauthorizedException.class, () -> cartRestController.addToCart(101L, session));
    }
}
