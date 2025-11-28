package com.example.smartshopapi.dto;

import com.example.smartshopapi.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponseDTO {
    private Long id;
    private Long clientId;
    private String clientName;
    private List<OrderItemResponseDTO> items;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal amountAfterDiscount;
    private BigDecimal tax;
    private BigDecimal totalAmount;
    private BigDecimal remainingAmount;
    private String promoCode;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}