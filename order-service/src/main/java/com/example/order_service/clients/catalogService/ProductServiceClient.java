package com.example.order_service.clients.catalogService;

import com.example.order_service.exception.ResourceNotFoundException;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Component
public class ProductServiceClient {
    private static final Logger log = LoggerFactory.getLogger(ProductServiceClient.class);
    private final RestClient restClient;

    ProductServiceClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @CircuitBreaker(name = "catalog-service")
    @Retry(name = "catalog-service", fallbackMethod = "getProductByCodeFallback")
    public Optional<Product> getProductByCode(String code) {
        log.info("Fetching product for code : {}", code);
        var product = restClient
                .get()
                .uri("/api/products/code/{code}", code)
                .retrieve()
                .body(Product.class);
        return Optional.ofNullable(product);
    }

    public Optional<Product> getProductByCodeFallback(String code, Throwable t) {
        log.error("Error fetching product for code: {}", code, t);
        throw new ResourceNotFoundException("Catalog service connection error :" + t.getMessage());
    }
}
