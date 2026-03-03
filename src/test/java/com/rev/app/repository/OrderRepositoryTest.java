package com.rev.app.repository;

import com.rev.app.entity.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryTest {

    @Mock
    private IOrderRepository orderRepository;

    @Test
    void testFindByUserUserId() {
        when(orderRepository.findByUser_UserId(1L))
                .thenReturn(List.of(new Order(), new Order()));

        List<Order> result = orderRepository.findByUser_UserId(1L);

        assertEquals(2, result.size());
    }
}
