package com.rev.app.service;

import com.rev.app.entity.Order;
import com.rev.app.entity.Payment;

public interface IPaymentService {

    Order checkoutWithPayment(Long userId,
                              String address,
                              String paymentMethod,
                              String cardHolderName,
                              String cardType,
                              String cardNumber,
                              String expiryDate,
                              String cvv);

    Payment getPaymentByOrderId(Long orderId);

    void updatePaymentStatus(Long orderId, String status);
}
