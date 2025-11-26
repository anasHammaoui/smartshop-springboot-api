package com.example.smartshopapi.service;

import com.example.smartshopapi.dto.ProductRequestDTO;
import com.example.smartshopapi.dto.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO productRequest);
    ProductResponseDTO getProductById(Long id);
    ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequest);
    void deleteProduct(Long id);
    Page<ProductResponseDTO> getAllProducts(Pageable pageable);
}