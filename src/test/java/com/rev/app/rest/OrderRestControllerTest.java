package com.rev.app.rest;

import com.rev.app.exception.UnauthorizedException;
import com.rev.app.service.IOrderService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderRestControllerTest {

    @Mock
    private IOrderService orderService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private OrderRestController orderRestController;

    @Test
    void testPlaceOrder_Success() {
        when(session.getAttribute("userId")).thenReturn(1L);
        String result = orderRestController.placeOrder(session);
        assertEquals("Order placed successfully", result);
        verify(orderService).placeOrder(eq(1L), anyString());
    }

    @Test
    void testUpdateStatus_AdminOnly() {
        when(session.getAttribute("role")).thenReturn("BUYER");
        assertThrows(UnauthorizedException.class, () -> 
            orderRestController.updateStatus(1L, "SHIPPED", session)
        );
    }
}
