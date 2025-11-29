package com.example.order_service.mapper;

import com.example.order_service.dto.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderEventMapper {
    public static OrderCreatedEvent buildOrderCreatedEvent(OrderDto orderDto) {
        return new OrderCreatedEvent(
                UUID.randomUUID().toString(),
                orderDto.orderNumber(),
                getOrderItems(orderDto),
                orderDto.customer(),
                orderDto.deliveryAddress(),
                LocalDateTime.now());
    }

    public static OrderDeliveredEvent buildOrderDeliveredEvent(OrderDto orderDto) {
        return new OrderDeliveredEvent(
                UUID.randomUUID().toString(),
                orderDto.orderNumber(),
                getOrderItems(orderDto),
                orderDto.customer(),
                orderDto.deliveryAddress(),
                LocalDateTime.now());
    }

    public static OrderCancelledEvent buildOrderCancelledEvent(OrderDto orderDto, String reason) {
        return new OrderCancelledEvent(
                UUID.randomUUID().toString(),
                orderDto.orderNumber(),
                getOrderItems(orderDto),
                orderDto.customer(),
                orderDto.deliveryAddress(),
                reason,
                LocalDateTime.now());
    }

    public static OrderErrorEvent buildOrderErrorEvent(OrderDto orderDto, String reason) {
        return new OrderErrorEvent(
                UUID.randomUUID().toString(),
                orderDto.orderNumber(),
                getOrderItems(orderDto),
                orderDto.customer(),
                orderDto.deliveryAddress(),
                reason,
                LocalDateTime.now());
    }

    private static Set<OrderItemDto> getOrderItems(OrderDto orderDto) {
        return orderDto.orderItems().stream()
                .map(item -> new OrderItemDto(item.code(), item.name(), item.price(), item.quantity()))
                .collect(Collectors.toSet());
    }
}
