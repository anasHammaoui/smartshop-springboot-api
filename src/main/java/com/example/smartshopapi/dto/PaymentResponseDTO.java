package com.example.smartshopapi.dto;

import com.example.smartshopapi.enums.PaymentStatus;
import com.example.smartshopapi.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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