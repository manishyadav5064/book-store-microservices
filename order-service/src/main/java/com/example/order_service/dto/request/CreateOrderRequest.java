package com.example.order_service.dto.request;

import com.example.order_service.dto.OrderItemDto;
import com.example.order_service.model.Address;
import com.example.order_service.model.Customer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CreateOrderRequest(
        @NotEmpty(message = "Items cannot be empty") Set<@Valid OrderItemDto> items,
        @NotNull(message = "customer details are required") @Valid Customer customer,
        @NotNull(message = "deliveryAddress is required") @Valid Address deliveryAddress,
        @Valid String comments
) {
}
