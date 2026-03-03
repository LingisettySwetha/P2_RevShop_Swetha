package com.rev.app.service;

import com.rev.app.entity.*;
import com.rev.app.exception.InvalidRequestException;
import com.rev.app.exception.ResourceNotFoundException;
import com.rev.app.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private IOrderRepository orderRepository;
    @Mock
    private IUserRepository userRepository;
    @Mock
    private ICartRepository cartRepository;
    @Mock
    private ICartItemRepository cartItemRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void testPlaceOrder_EmptyCart() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(cartRepository.findByUser_UserId(1L)).thenReturn(null);

        assertThrows(InvalidRequestException.class, () -> orderService.placeOrder(1L, "Test Address"));
    }

    @Test
    void testGetOrderById_Success() {
        Order order = new Order();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        
        Order found = orderService.getOrderById(1L);
        assertNotNull(found);
    }
}
