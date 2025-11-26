package com.example.smartshopapi.controller;

import com.example.smartshopapi.dto.ProductRequestDTO;
import com.example.smartshopapi.dto.ProductResponseDTO;
import com.example.smartshopapi.service.ProductService;
import com.example.smartshopapi.service.SecurityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    private final SecurityService securityService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDTO createProduct(@Valid @RequestBody ProductRequestDTO productRequest, HttpServletRequest request) {
        securityService.requireAdmin(request);
        return productService.createProduct(productRequest);
    }
    
    @GetMapping("/{id}")
    public ProductResponseDTO getProduct(@PathVariable Long id, HttpServletRequest request) {
        // Accessible aux clients et admins
        securityService.requireAuthenticated(request);
        return productService.getProductById(id);
    }
    
    @PutMapping("/{id}")
    public ProductResponseDTO updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO productRequest, HttpServletRequest request) {
        securityService.requireAdmin(request);
        return productService.updateProduct(id, productRequest);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id, HttpServletRequest request) {
        securityService.requireAdmin(request);
        productService.deleteProduct(id);
    }
    
    @GetMapping
    public Page<ProductResponseDTO> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        // Accessible aux clients et admins
        securityService.requireAuthenticated(request);
        Pageable pageable = PageRequest.of(page, size);
        return productService.getAllProducts(pageable);
    }
}