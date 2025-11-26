package com.example.smartshopapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    
    @NotNull(message = "Client ID is required")
    private Long clientId;
    
    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemRequestDTO> items;
    
    @Pattern(regexp = "PROMO-[A-Z0-9]{4}", message = "Promo code must follow format PROMO-XXXX")
    private String promoCode;
}