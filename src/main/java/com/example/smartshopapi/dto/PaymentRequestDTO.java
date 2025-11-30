package com.example.smartshopapi.dto;

import com.example.smartshopapi.enums.PaymentType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentRequestDTO {
    
    @NotNull(message = "Order ID is required")
    private Long orderId;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    @NotNull(message = "Payment type is required")
    private PaymentType paymentType;

    private LocalDateTime paymentDate;
    
    private String reference;
    private String bank;
    private LocalDateTime dueDate;
}