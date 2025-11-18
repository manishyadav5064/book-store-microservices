package com.example.order_service.dto;

import com.example.order_service.model.Address;
import com.example.order_service.model.Customer;
import com.example.order_service.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.Set;

public record OrderDto(
        Long id,
        String orderNumber,
        String username,
        Customer customer,
        Address deliveryAddress,
        Set<OrderItemDto> orderItems,
        OrderStatus status,
        String comments,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
