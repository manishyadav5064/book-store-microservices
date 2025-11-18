package com.example.order_service.service;

import com.example.order_service.dto.OrderDto;
import com.example.order_service.dto.request.CreateOrderRequest;

public interface OrderService {
    OrderDto createOrder(String username, CreateOrderRequest request);

    OrderDto getOrderById(Long id);
}
