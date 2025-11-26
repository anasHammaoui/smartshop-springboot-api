package com.example.smartshopapi.service;

import com.example.smartshopapi.dto.PaymentRequestDTO;
import com.example.smartshopapi.dto.PaymentResponseDTO;
import com.example.smartshopapi.enums.PaymentStatus;

import java.util.List;

public interface PaymentService {
    PaymentResponseDTO addPayment(PaymentRequestDTO paymentRequest);
    List<PaymentResponseDTO> getPaymentsByOrderId(Long orderId);
    PaymentResponseDTO updatePaymentStatus(Long paymentId, PaymentStatus status);
}