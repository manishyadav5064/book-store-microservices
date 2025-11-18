package com.example.order_service.mapper;

import com.example.order_service.dto.*;
import com.example.order_service.dto.request.CreateOrderRequest;
import com.example.order_service.model.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderMapper {

    public static Order toEntity(CreateOrderRequest req) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setStatus(OrderStatus.NEW);
        order.setComments(req.comments());
        order.setCustomer(new Customer(
                req.customer().name(),
                req.customer().email(),
                req.customer().phone()
        ));
        order.setDeliveryAddress(new Address(
                req.deliveryAddress().addressLine1(),
                req.deliveryAddress().addressLine2(),
                req.deliveryAddress().city(),
                req.deliveryAddress().state(),
                req.deliveryAddress().zipcode(),
                req.deliveryAddress().country()
        ));
        // Items
        Set<OrderItem> itemSet = req.items().stream().map(itemDto -> {
            OrderItem item = new OrderItem();
            item.setCode(itemDto.code());
            item.setName(itemDto.name());
            item.setPrice(itemDto.price());
            item.setQuantity(itemDto.quantity());
            item.setOrder(order);
            return item;
        }).collect(Collectors.toSet());

        order.setOrderItems(itemSet);

        return order;
    }

    public static OrderDto toDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getOrderNumber(),
                order.getUsername(),
                new Customer(
                        order.getCustomer().name(),
                        order.getCustomer().email(),
                        order.getCustomer().phone()
                ),
                new Address(
                        order.getDeliveryAddress().addressLine1(),
                        order.getDeliveryAddress().addressLine2(),
                        order.getDeliveryAddress().city(),
                        order.getDeliveryAddress().state(),
                        order.getDeliveryAddress().zipcode(),
                        order.getDeliveryAddress().country()
                ),
                order.getOrderItems().stream().map(i ->
                        new OrderItemDto(i.getCode(), i.getName(), i.getPrice(), i.getQuantity())
                ).collect(Collectors.toSet()),
                order.getStatus(),
                order.getComments(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
