package com.example.smartshopapi.dto;

import com.example.smartshopapi.enums.PaymentStatus;
import com.example.smartshopapi.enums.PaymentType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponseDTO {
    private Long id;
    private Long orderId;
    private Integer paymentNumber;
    private BigDecimal amount;
    private PaymentType paymentType;
    private LocalDateTime paymentDate;
    private LocalDateTime encashmentDate;
    private String reference;
    private String bank;
    private LocalDateTime dueDate;
    private PaymentStatus status;
    private LocalDateTime createdAt;
}