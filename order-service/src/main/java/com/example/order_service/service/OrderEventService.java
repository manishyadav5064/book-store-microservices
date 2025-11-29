package com.example.order_service.service;

import com.example.order_service.dto.OrderCancelledEvent;
import com.example.order_service.dto.OrderCreatedEvent;
import com.example.order_service.dto.OrderDeliveredEvent;
import com.example.order_service.dto.OrderErrorEvent;

public interface OrderEventService {
    void save(OrderCreatedEvent orderCreatedEvent);

    void save(OrderDeliveredEvent orderDeliveredEvent);

    void save(OrderCancelledEvent orderCancelledEvent);

    void save(OrderErrorEvent orderErrorEvent);

    void publishOrderEvents();
}