package com.example.order_service.clients.catalogService;

import java.math.BigDecimal;

public record Product(String code,
                      String name,
                      String description,
                      String imageUrl,
                      BigDecimal price) {
}