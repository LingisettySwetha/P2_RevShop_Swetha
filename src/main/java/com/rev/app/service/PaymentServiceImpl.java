package com.rev.app.service;

import com.rev.app.entity.Order;
import com.rev.app.entity.Payment;
import com.rev.app.exception.InvalidRequestException;
import com.rev.app.exception.ResourceNotFoundException;
import com.rev.app.repository.IPaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PaymentServiceImpl implements IPaymentService {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IPaymentRepository paymentRepository;

    @Override
    @Transactional
    public Order checkoutWithPayment(Long userId,
                                     String address,
                                     String paymentMethod,
                                     String cardNumber,
                                     String expiryDate,
                                     String cvv) {

        validateCheckoutInput(address, paymentMethod, cardNumber, expiryDate, cvv);

        Order order = orderService.placeOrder(userId, address.trim());

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(normalizeMethod(paymentMethod));
        payment.setPaymentStatus("PAID");

        order.setPayment(payment);
        paymentRepository.save(payment);

        log.info("Payment completed for order {}", order.getOrderId());
        return order;
    }

    @Override
    public Payment getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrder_OrderId(orderId);
        if (payment == null) {
            throw new ResourceNotFoundException("Payment not found for order id: " + orderId);
        }
        return payment;
    }

    @Override
    public void updatePaymentStatus(Long orderId, String status) {
        Payment payment = paymentRepository.findByOrder_OrderId(orderId);
        if (payment == null) {
            throw new ResourceNotFoundException("Payment not found for order id: " + orderId);
        }
        payment.setPaymentStatus(status);
        paymentRepository.save(payment);
    }

    private void validateCheckoutInput(String address,
                                       String paymentMethod,
                                       String cardNumber,
                                       String expiryDate,
                                       String cvv) {
        if (address == null || address.isBlank()) {
            throw new InvalidRequestException("Shipping address is required");
        }
        if (paymentMethod == null || paymentMethod.isBlank()) {
            throw new InvalidRequestException("Payment method is required");
        }
        if ("CARD".equalsIgnoreCase(paymentMethod)) {
            if (cardNumber == null || !cardNumber.replaceAll("\\s+", "").matches("\\d{16}")) {
                throw new InvalidRequestException("Card number must be 16 digits");
            }
            if (expiryDate == null || !expiryDate.matches("(0[1-9]|1[0-2])\\s*/\\s*\\d{2}")) {
                throw new InvalidRequestException("Expiry must be in MM/YY format");
            }
            if (cvv == null || !cvv.matches("\\d{3}")) {
                throw new InvalidRequestException("CVV must be 3 digits");
            }
        }
    }

    private String normalizeMethod(String method) {
        String cleaned = method.trim().toUpperCase();
        if (cleaned.equals("VISA") || cleaned.equals("MASTER") || cleaned.equals("AMEX")) {
            return "CARD";
        }
        return cleaned;
    }
}
