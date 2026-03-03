package com.rev.app.repository;

import com.rev.app.entity.Payment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentRepositoryTest {

    @Mock
    private IPaymentRepository paymentRepository;

    @Test
    void testFindByOrderOrderId() {
        Payment payment = new Payment();
        payment.setPaymentStatus("PAID");

        when(paymentRepository.findByOrder_OrderId(1L)).thenReturn(payment);

        Payment result = paymentRepository.findByOrder_OrderId(1L);
        assertEquals("PAID", result.getPaymentStatus());
    }
}
