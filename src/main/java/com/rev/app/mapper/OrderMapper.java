package com.rev.app.mapper;

import com.rev.app.dto.*;
import com.rev.app.entity.*;

import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDTO toDTO(Order order){

        OrderDTO dto=new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderStatus(order.getOrderStatus() != null ? order.getOrderStatus().name() : null);
        dto.setOrderDate(order.getOrderDate());

        dto.setItems(
            order.getOrderItems()
            .stream()
            .map(i->{
                OrderItemDTO d=new OrderItemDTO();
                d.setOrderItemId(i.getOrderItemId());
                d.setProductId(i.getProduct().getProductId());
                d.setProductName(i.getProduct().getName());
                d.setQuantity(i.getQuantity());
                d.setPrice(i.getPrice());
                d.setSubtotal(i.getSubtotal());
                return d;
            }).collect(Collectors.toList())
        );

        return dto;
    }
}
