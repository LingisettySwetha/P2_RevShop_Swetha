package com.rev.app.service;

import com.rev.app.entity.Order;
import com.rev.app.repository.IOrderRepository;
import com.rev.app.repository.IProductRepository;
import com.rev.app.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IProductRepository productRepository;

    @Mock
    private IOrderRepository orderRepository;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Test
    void testTotalUsers() {
        when(userRepository.count()).thenReturn(10L);
        assertEquals(10L, dashboardService.totalUsers());
    }

    @Test
    void testTotalRevenue() {
        Order o1 = new Order(); o1.setTotalAmount(100.0);
        Order o2 = new Order(); o2.setTotalAmount(200.0);
        when(orderRepository.findAll()).thenReturn(List.of(o1, o2));
        
        assertEquals(300.0, dashboardService.totalRevenue());
    }
}
