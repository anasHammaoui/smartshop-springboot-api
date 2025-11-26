package com.example.smartshopapi.dto;

import com.example.smartshopapi.enums.CustomerTier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientResponseDTO {
    private Long id;
    private String name;
    private String email;
    private CustomerTier tier;
    private Integer totalOrders;
    private BigDecimal totalSpent;
    private LocalDateTime firstOrderDate;
    private LocalDateTime lastOrderDate;
    private LocalDateTime createdAt;
}