package com.rev.app.service;

import com.rev.app.entity.Order;
import com.rev.app.entity.Payment;
import com.rev.app.exception.InvalidRequestException;
import com.rev.app.exception.ResourceNotFoundException;
import com.rev.app.repository.IPaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private IOrderService orderService;

    @Mock
    private IPaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void testCheckoutWithPayment_Success() {
        Order order = new Order();
        order.setOrderId(1L);
        when(orderService.placeOrder(1L, "Address")).thenReturn(order);

        Order result = paymentService.checkoutWithPayment(
                1L, "Address", "CREDIT_CARD", "John Doe", "SBI Credit Card", "4111111111111111", "12/30", "123");

        assertEquals(1L, result.getOrderId());
        verify(paymentRepository).save(org.mockito.ArgumentMatchers.any(Payment.class));
    }

    @Test
    void testCheckoutWithPayment_InvalidCard() {
        assertThrows(InvalidRequestException.class, () ->
                paymentService.checkoutWithPayment(
                        1L, "Address", "CREDIT_CARD", "John Doe", "SBI Credit Card", "1234", "12/30", "123"));
    }

    @Test
    void testCheckoutWithPayment_CodSuccess() {
        Order order = new Order();
        order.setOrderId(2L);
        when(orderService.placeOrder(1L, "Address")).thenReturn(order);

        paymentService.checkoutWithPayment(
                1L, "Address", "CASH_ON_DELIVERY", null, null, null, null, null);

        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(captor.capture());
        assertEquals("CASH_ON_DELIVERY", captor.getValue().getPaymentMethod());
        assertEquals("PENDING", captor.getValue().getPaymentStatus());
    }

    @Test
    void testGetPaymentByOrderId_NotFound() {
        when(paymentRepository.findByOrder_OrderId(100L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class,
                () -> paymentService.getPaymentByOrderId(100L));
    }
}
