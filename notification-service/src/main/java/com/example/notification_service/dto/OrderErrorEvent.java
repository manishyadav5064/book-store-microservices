package com.example.notification_service.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record OrderErrorEvent(
        String eventId,
        String orderNumber,
        Set<OrderItemDto> orderItems,
        Customer customer,
        Address deliveryAddress,
        String reason,
        LocalDateTime createdAt) {
}