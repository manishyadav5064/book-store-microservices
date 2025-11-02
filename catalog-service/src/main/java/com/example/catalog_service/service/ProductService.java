package com.example.catalog_service.service;

import com.example.catalog_service.dto.ProductRequest;
import com.example.catalog_service.dto.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(Long id, ProductRequest request);

    ProductResponse getProduct(Long id);

    Page<ProductResponse> getAllProducts(int page, int size, String sortBy);

    void deleteProduct(Long id);
}
