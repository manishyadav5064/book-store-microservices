package com.example.notification_service.events;

import com.example.notification_service.dto.OrderCancelledEvent;
import com.example.notification_service.dto.OrderCreatedEvent;
import com.example.notification_service.dto.OrderDeliveredEvent;
import com.example.notification_service.dto.OrderErrorEvent;
import com.example.notification_service.model.OrderEvent;
import com.example.notification_service.repository.OrderEventRepository;
import com.example.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventHandler {
    private static final Logger log = LoggerFactory.getLogger(OrderEventHandler.class);

    private final NotificationService notificationService;
    private final OrderEventRepository orderEventRepository;

    @RabbitListener(queues = "${notifications.new-orders-queue}")
    void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Order created event for orderNumber: {}", event.orderNumber());
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderCreatedEvent with eventId : {}", event.eventId());
            return;
        }
        notificationService.sendOrderCreatedNotification(event);
        orderEventRepository.save(new OrderEvent(event.eventId()));
    }

    @RabbitListener(queues = "${notifications.delivered-orders-queue}")
    void handleOrderDeliveredEvent(OrderDeliveredEvent event) {
        log.info("Order delivered event for orderNumber: {}", event.orderNumber());
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderDeliveredEvent with eventId : {}", event.eventId());
            return;
        }
        notificationService.sendOrderDeliveredNotification(event);
        orderEventRepository.save(new OrderEvent(event.eventId()));
    }

    @RabbitListener(queues = "${notifications.cancelled-orders-queue}")
    void handleOrderCancelledEvent(OrderCancelledEvent event) {
        log.info("Order cancelled event for orderNumber: {}", event.orderNumber());
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderCancelledEvent with eventId : {}", event.eventId());
            return;
        }
        notificationService.sendOrderCancelledNotification(event);
        orderEventRepository.save(new OrderEvent(event.eventId()));
    }

    @RabbitListener(queues = "${notifications.error-orders-queue}")
    void handleOrderErrorEvent(OrderErrorEvent event) {
        log.info("Order error event for orderNumber: {}", event.orderNumber());
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderErrorEvent with eventId : {}", event.eventId());
            return;
        }
        notificationService.sendOrderErrorNotification(event);
        orderEventRepository.save(new OrderEvent(event.eventId()));
    }
}
