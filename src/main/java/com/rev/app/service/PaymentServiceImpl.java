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
                                     String cardHolderName,
                                     String cardType,
                                     String cardNumber,
                                     String expiryDate,
                                     String cvv) {

        validateCheckoutInput(address, paymentMethod, cardHolderName, cardType, cardNumber, expiryDate, cvv);

        Order order = orderService.placeOrder(userId, address.trim());

        String normalizedMethod = normalizeMethod(paymentMethod);
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(normalizedMethod);
        payment.setPaymentStatus("CASH_ON_DELIVERY".equals(normalizedMethod) ? "PENDING" : "PAID");

        if (isCardMethod(normalizedMethod)) {
            payment.setCardHolderName(cardHolderName.trim());
            payment.setCardType(cardType.trim());
            payment.setMaskedCardNumber(maskCardNumber(cardNumber));
            payment.setCardExpiry(expiryDate.replaceAll("\\s+", ""));
        }

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
                                       String cardHolderName,
                                       String cardType,
                                       String cardNumber,
                                       String expiryDate,
                                       String cvv) {
        if (address == null || address.isBlank()) {
            throw new InvalidRequestException("Shipping address is required");
        }
        if (paymentMethod == null || paymentMethod.isBlank()) {
            throw new InvalidRequestException("Payment method is required");
        }

        String normalizedMethod = normalizeMethod(paymentMethod);
        if (!"CREDIT_CARD".equals(normalizedMethod)
                && !"DEBIT_CARD".equals(normalizedMethod)
                && !"CASH_ON_DELIVERY".equals(normalizedMethod)) {
            throw new InvalidRequestException("Unsupported payment method");
        }

        if (isCardMethod(normalizedMethod)) {
            if (cardHolderName == null || cardHolderName.isBlank()) {
                throw new InvalidRequestException("Card holder name is required");
            }
            if (cardType == null || cardType.isBlank()) {
                throw new InvalidRequestException("Card type is required");
            }
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
        if ("CARD".equals(cleaned) || "CREDIT".equals(cleaned) || "CREDITCARD".equals(cleaned)) {
            return "CREDIT_CARD";
        }
        if ("DEBIT".equals(cleaned) || "DEBITCARD".equals(cleaned)) {
            return "DEBIT_CARD";
        }
        if ("COD".equals(cleaned) || "CASH ON DELIVERY".equals(cleaned)) {
            return "CASH_ON_DELIVERY";
        }
        if (cleaned.equals("VISA") || cleaned.equals("MASTER") || cleaned.equals("AMEX")) {
            return "CREDIT_CARD";
        }
        return cleaned;
    }

    private boolean isCardMethod(String paymentMethod) {
        return "CREDIT_CARD".equals(paymentMethod) || "DEBIT_CARD".equals(paymentMethod);
    }

    private String maskCardNumber(String cardNumber) {
        String digits = cardNumber.replaceAll("\\s+", "");
        return "**** **** **** " + digits.substring(digits.length() - 4);
    }
}
