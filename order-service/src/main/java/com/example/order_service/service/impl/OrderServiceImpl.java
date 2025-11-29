package com.example.order_service.service.impl;

import com.example.order_service.dto.OrderCreatedEvent;
import com.example.order_service.dto.OrderDto;
import com.example.order_service.dto.request.CreateOrderRequest;
import com.example.order_service.mapper.OrderEventMapper;
import com.example.order_service.mapper.OrderMapper;
import com.example.order_service.model.Order;
import com.example.order_service.model.OrderStatus;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.service.OrderEventService;
import com.example.order_service.service.OrderService;
import com.example.order_service.service.OrderValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderEventService orderEventService;

    @Override
    public OrderDto createOrder(String username, CreateOrderRequest request) {
        orderValidator.validate(request);
        Order order = OrderMapper.toEntity(request);
        order.setUsername(username);
        Order saved = orderRepository.save(order);
        log.info("created order with order number : {}", saved.getOrderNumber());

        OrderCreatedEvent orderCreatedEvent = OrderEventMapper.buildOrderCreatedEvent(OrderMapper.toDto(order));
        orderEventService.save(orderCreatedEvent);

        return OrderMapper.toDto(saved);
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));

        return OrderMapper.toDto(order);
    }

    @Override
    public void processNewOrders() {
        List<Order> orders = orderRepository.findByStatus(OrderStatus.NEW);
        log.info("Found {} orders to process", orders.size());
        for (Order order : orders) {
            this.process(order);
        }
    }

    private void process(Order order) {
        try {
            if (canBeDelivered(order)) {
                log.info("order number {} can be delivered", order.getOrderNumber());
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.DELIVERED);
                orderEventService.save(OrderEventMapper.buildOrderDeliveredEvent(OrderMapper.toDto(order)));
            } else {
                log.info("order number {} cannot be delivered", order.getOrderNumber());
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.CANCELLED);
                orderEventService.save(OrderEventMapper.buildOrderCancelledEvent(OrderMapper.toDto(order), "can't deliver to this location"));
            }
        } catch (Exception e) {
            log.info("order number {} failed to process", order.getOrderNumber());
            orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.ERROR);
            orderEventService.save(OrderEventMapper.buildOrderErrorEvent(OrderMapper.toDto(order), e.getMessage()));

        }
    }

    private boolean canBeDelivered(Order order) {
        return order.getDeliveryAddress().country().equalsIgnoreCase("INDIA");
    }
}
