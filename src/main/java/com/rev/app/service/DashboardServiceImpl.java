package com.rev.app.service;

import com.rev.app.repository.IOrderRepository;
import com.rev.app.repository.IProductRepository;
import com.rev.app.repository.IUserRepository;
import com.rev.app.service.IDashboardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DashboardServiceImpl implements IDashboardService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Override
    public long totalUsers() {
        log.info("Calculating total users count");
        return userRepository.count();
    }

    @Override
    public long totalProducts() {
        return productRepository.count();
    }

    @Override
    public long totalOrders() {
        return orderRepository.count();
    }

    @Override
    public double totalRevenue() {
        log.info("Calculating total revenue from orders");
        return orderRepository.findAll()
                .stream()
                .mapToDouble(o -> o.getTotalAmount())
                .sum();
    }
}