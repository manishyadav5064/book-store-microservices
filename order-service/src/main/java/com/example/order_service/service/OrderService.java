package com.example.order_service.service;

import com.example.order_service.dto.OrderDto;
import com.example.order_service.dto.OrderSummary;
import com.example.order_service.dto.request.CreateOrderRequest;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(String username, CreateOrderRequest request);

    OrderDto getOrderById(Long id);

    void processNewOrders();

    List<OrderSummary> getOrders(String username);

    OrderDto getOrderByOrderNumber(String username, String orderNumber);
}
