package com.rev.app.rest;

import com.rev.app.dto.PlaceOrderRequest;
import com.rev.app.entity.Order;
import com.rev.app.entity.User;
import com.rev.app.exception.UnauthorizedException;
import com.rev.app.service.IOrderService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        when(session.getAttribute("role")).thenReturn("BUYER");

        Order order = new Order();
        order.setOrderNumber("RS-20260313-111111");
        order.setUser(new User());
        when(orderService.placeOrder(eq(1L), anyString())).thenReturn(order);

        PlaceOrderRequest request = new PlaceOrderRequest();
        request.setAddress("Test Address");

        var result = orderRestController.placeOrder(request, session);
        assertNotNull(result);
        assertEquals("RS-20260313-111111", result.getOrderNumber());
        verify(orderService).placeOrder(1L, "Test Address");
    }

    @Test
    void testCancelOrder_BuyerOnly() {
        when(session.getAttribute("userId")).thenReturn(1L);
        when(session.getAttribute("role")).thenReturn("BUYER");

        Order order = new Order();
        order.setOrderNumber("RS-20260313-222222");
        when(orderService.cancelOrder(1L, 1L)).thenReturn(order);

        var result = orderRestController.cancelOrder(1L, session);
        assertNotNull(result);
        verify(orderService).cancelOrder(1L, 1L);
    }

    @Test
    void testPlaceOrder_Unauthorized() {
        when(session.getAttribute("userId")).thenReturn(null);
        PlaceOrderRequest request = new PlaceOrderRequest();
        request.setAddress("Address");
        assertThrows(UnauthorizedException.class, () ->
                orderRestController.placeOrder(request, session)
        );
    }
}
