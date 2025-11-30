package com.example.smartshopapi.service.impl;

import com.example.smartshopapi.dto.ProductRequestDTO;
import com.example.smartshopapi.dto.ProductResponseDTO;
import com.example.smartshopapi.entity.Product;
import com.example.smartshopapi.exception.ResourceNotFoundException;
import com.example.smartshopapi.mapper.ProductMapper;
import com.example.smartshopapi.repository.ProductRepository;
import com.example.smartshopapi.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    
    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequest) {
        Product product = productMapper.toEntity(productRequest);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponseDTO(savedProduct);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findActiveById(id);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
        return productMapper.toResponseDTO(product);
    }
    
    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequest) {
        Product product = productRepository.findActiveById(id);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
        
        productMapper.updateEntityFromDTO(productRequest, product);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponseDTO(updatedProduct);
    }
    
    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findActiveById(id);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
        
        product.setDeleted(true);
        productRepository.save(product);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAllActive(pageable)
                .map(productMapper::toResponseDTO);
    }
}