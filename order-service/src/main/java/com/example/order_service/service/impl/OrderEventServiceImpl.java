package com.example.order_service.service.impl;

import com.example.order_service.dto.OrderCancelledEvent;
import com.example.order_service.dto.OrderCreatedEvent;
import com.example.order_service.dto.OrderDeliveredEvent;
import com.example.order_service.dto.OrderErrorEvent;
import com.example.order_service.messaging.OrderEventPublisher;
import com.example.order_service.model.OrderEvent;
import com.example.order_service.model.OrderEventType;
import com.example.order_service.repository.OrderEventRepository;
import com.example.order_service.service.OrderEventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderEventServiceImpl implements OrderEventService {
    private static final Logger log = LoggerFactory.getLogger(OrderEventServiceImpl.class);

    private final OrderEventRepository orderEventRepository;
    private final OrderEventPublisher orderEventPublisher;
    private final ObjectMapper objectMapper;

    @Override
    public void save(OrderCreatedEvent orderCreatedEvent) {
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setEventId(orderCreatedEvent.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_CREATED);
        orderEvent.setOrderNumber(orderCreatedEvent.orderNumber());
        orderEvent.setCreatedAt(orderCreatedEvent.createdAt());
        orderEvent.setPayload(toJsonPayload(orderCreatedEvent));
        this.orderEventRepository.save(orderEvent);
    }

    @Override
    public void save(OrderCancelledEvent event) {
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_CANCELLED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(orderEvent);
    }

    @Override
    public void save(OrderDeliveredEvent event) {
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_DELIVERED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(orderEvent);
    }


    @Override
    public void save(OrderErrorEvent event) {
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_PROCESSING_FAILED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(orderEvent);
    }

    @Override
    public void publishOrderEvents() {
        Sort sort = Sort.by("createdAt").ascending();
        List<OrderEvent> orderEventList = orderEventRepository.findAll(sort);
        log.info("Found {} order events to be published", orderEventList.size());
        for (OrderEvent orderEvent : orderEventList) {
            this.publishEvent(orderEvent);
            orderEventRepository.delete(orderEvent);
        }
    }

    private void publishEvent(OrderEvent orderEvent) {
        OrderEventType orderEventType = orderEvent.getEventType();
        switch (orderEventType) {
            case ORDER_CREATED:
                OrderCreatedEvent orderCreatedEvent = fromJsonPayload(orderEvent.getPayload(), OrderCreatedEvent.class);
                orderEventPublisher.publish(orderCreatedEvent);
                break;
            case ORDER_DELIVERED:
                OrderDeliveredEvent orderDeliveredEvent =
                        fromJsonPayload(orderEvent.getPayload(), OrderDeliveredEvent.class);
                orderEventPublisher.publish(orderDeliveredEvent);
                break;
            case ORDER_CANCELLED:
                OrderCancelledEvent orderCancelledEvent =
                        fromJsonPayload(orderEvent.getPayload(), OrderCancelledEvent.class);
                orderEventPublisher.publish(orderCancelledEvent);
                break;
            case ORDER_PROCESSING_FAILED:
                OrderErrorEvent orderErrorEvent = fromJsonPayload(orderEvent.getPayload(), OrderErrorEvent.class);
                orderEventPublisher.publish(orderErrorEvent);
                break;
            default:
                log.warn("Unsupported OrderEventType: {}", orderEventType);
        }
    }

    private String toJsonPayload(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T fromJsonPayload(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}