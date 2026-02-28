package com.rev.app.service;

import com.rev.app.repository.IOrderRepository;
import com.rev.app.repository.IProductRepository;
import com.rev.app.repository.IUserRepository;
import com.rev.app.service.IDashboardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        return orderRepository.findAll()
                .stream()
                .mapToDouble(o -> o.getTotalAmount())
                .sum();
    }
}