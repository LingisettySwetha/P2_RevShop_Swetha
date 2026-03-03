package com.rev.app.repository;

import com.rev.app.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderRepository
        extends JpaRepository<Order, Long> {

    List<Order> findByUser_UserId(Long userId);
}