package com.example.smartshopapi.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductResponseDTO {
    private Long id;
    private String name;
    private BigDecimal unitPrice;
    private Integer stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}