package com.rev.app.repository;

import com.rev.app.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByOrder_OrderId(Long orderId);
}
