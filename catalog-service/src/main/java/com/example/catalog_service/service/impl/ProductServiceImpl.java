package com.example.catalog_service.service.impl;

import com.example.catalog_service.dto.ProductRequest;
import com.example.catalog_service.dto.ProductResponse;
import com.example.catalog_service.exception.ResourceNotFoundException;
import com.example.catalog_service.mapper.ProductMapper;
import com.example.catalog_service.model.Product;
import com.example.catalog_service.respository.ProductRepository;
import com.example.catalog_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Product product = ProductMapper.toEntity(request);
        Product saved = productRepository.save(product);

        return ProductMapper.toResponse(saved);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        ProductMapper.updateEntity(product, request); // this will update product object

        return ProductMapper.toResponse(productRepository.save(product)); // this will save updated product in db
    }

    @Override
    public ProductResponse getProduct(Long id) {
        return productRepository.findById(id)
                .map(ProductMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    public ProductResponse getProduct(String code) {
        return productRepository.findByCode(code)
                .map(ProductMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with code: " + code));
    }

    @Override
    public Page<ProductResponse> getAllProducts(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Product> productPage = productRepository.findAll(pageable);

        return productPage.map(ProductMapper::toResponse);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
