package com.rev.app.rest;

import com.rev.app.dto.PaymentCheckoutRequest;
import com.rev.app.entity.Order;
import com.rev.app.exception.UnauthorizedException;
import com.rev.app.service.IOrderService;
import com.rev.app.service.IPaymentService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentRestControllerTest {

    @Mock
    private IPaymentService paymentService;

    @Mock
    private IOrderService orderService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private PaymentRestController paymentRestController;

    @Test
    void testCheckout_Success() {
        PaymentCheckoutRequest request = new PaymentCheckoutRequest();
        request.setAddress("Address");
        request.setPaymentMethod("CARD");
        request.setCardNumber("4111111111111111");
        request.setExpiryDate("12/30");
        request.setCvv("123");

        Order order = new Order();
        order.setOrderId(10L);

        when(session.getAttribute("userId")).thenReturn(1L);
        when(session.getAttribute("role")).thenReturn("BUYER");
        when(paymentService.checkoutWithPayment(1L, "Address", "CARD", "4111111111111111", "12/30", "123"))
                .thenReturn(order);

        Map<String, Object> response = paymentRestController.checkout(request, session);
        assertEquals(10L, response.get("orderId"));
        assertEquals("PAID", response.get("paymentStatus"));
    }

    @Test
    void testCheckout_UnauthorizedRole() {
        PaymentCheckoutRequest request = new PaymentCheckoutRequest();
        request.setAddress("Address");
        request.setPaymentMethod("CARD");

        when(session.getAttribute("userId")).thenReturn(1L);
        when(session.getAttribute("role")).thenReturn("SELLER");

        assertThrows(UnauthorizedException.class,
                () -> paymentRestController.checkout(request, session));
    }
}
