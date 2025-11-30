package com.example.order_service.dto;

import com.example.order_service.model.OrderStatus;

public record OrderSummary(
        String orderNumber,
        OrderStatus orderStatus
) {
}