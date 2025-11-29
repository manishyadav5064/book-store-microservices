package com.example.order_service.service;

import com.example.order_service.clients.catalogService.Product;
import com.example.order_service.clients.catalogService.ProductServiceClient;
import com.example.order_service.dto.OrderItemDto;
import com.example.order_service.dto.request.CreateOrderRequest;
import com.example.order_service.exception.InvalidOrderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class OrderValidator {
    private static final Logger log = LoggerFactory.getLogger(OrderValidator.class);
    private final ProductServiceClient productServiceClient;

    OrderValidator(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }

    public void validate(CreateOrderRequest createOrderRequest) {
        Set<OrderItemDto> orderItemSet = createOrderRequest.items();
        for (OrderItemDto itemDto : orderItemSet) {
            Product product = productServiceClient.getProductByCode(itemDto.code())
                    .orElseThrow(() -> new InvalidOrderException("Invalid product code: " + itemDto.code()));
            if (itemDto.price().compareTo(product.price()) != 0) {
                log.error("Product price mismatch. Actual price is {}, received price is {}", product.price(), itemDto.price());
                throw new InvalidOrderException("Product price mismatch for product code : " + itemDto.code());
            }
        }
    }
}
