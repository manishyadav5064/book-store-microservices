package com.example.notification_service.service;

import com.example.notification_service.dto.OrderCancelledEvent;
import com.example.notification_service.dto.OrderCreatedEvent;
import com.example.notification_service.dto.OrderDeliveredEvent;
import com.example.notification_service.dto.OrderErrorEvent;

public interface NotificationService {
    void sendOrderCreatedNotification(OrderCreatedEvent event);

    void sendOrderDeliveredNotification(OrderDeliveredEvent event);

    void sendOrderCancelledNotification(OrderCancelledEvent event);

    void sendOrderErrorNotification(OrderErrorEvent event);
}