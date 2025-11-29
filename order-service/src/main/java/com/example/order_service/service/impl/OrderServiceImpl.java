package com.example.order_service.service.impl;

import com.example.order_service.dto.OrderDto;
import com.example.order_service.dto.request.CreateOrderRequest;
import com.example.order_service.mapper.OrderMapper;
import com.example.order_service.model.Order;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.service.OrderService;
import com.example.order_service.service.OrderValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    @Override
    public OrderDto createOrder(String username, CreateOrderRequest request) {
        orderValidator.validate(request);
        Order order = OrderMapper.toEntity(request);
        order.setUsername(username);
        Order saved = orderRepository.save(order);
        log.info("created order with order number : {}", saved.getOrderNumber());

        return OrderMapper.toDto(saved);
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));

        return OrderMapper.toDto(order);
    }
}
