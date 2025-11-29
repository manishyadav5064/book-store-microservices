package com.example.order_service.dto;

import com.example.order_service.model.Address;
import com.example.order_service.model.Customer;

import java.time.LocalDateTime;
import java.util.Set;

public record OrderCancelledEvent(
        String eventId,
        String orderNumber,
        Set<OrderItemDto> orderItems,
        Customer customer,
        Address deliveryAddress,
        String reason,
        LocalDateTime createdAt) {
}