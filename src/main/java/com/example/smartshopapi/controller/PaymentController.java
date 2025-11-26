package com.example.smartshopapi.controller;

import com.example.smartshopapi.dto.PaymentRequestDTO;
import com.example.smartshopapi.dto.PaymentResponseDTO;
import com.example.smartshopapi.enums.PaymentStatus;
import com.example.smartshopapi.service.PaymentService;
import com.example.smartshopapi.service.SecurityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    
    private final PaymentService paymentService;
    private final SecurityService securityService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponseDTO addPayment(@Valid @RequestBody PaymentRequestDTO paymentRequest, HttpServletRequest request) {
        securityService.requireAdmin(request);
        return paymentService.addPayment(paymentRequest);
    }
    
    @GetMapping("/order/{orderId}")
    public List<PaymentResponseDTO> getPaymentsByOrder(@PathVariable Long orderId, HttpServletRequest request) {
        securityService.requireAdmin(request);
        return paymentService.getPaymentsByOrderId(orderId);
    }
    
    @PutMapping("/{paymentId}/status")
    public PaymentResponseDTO updatePaymentStatus(
            @PathVariable Long paymentId,
            @RequestParam PaymentStatus status,
            HttpServletRequest request) {
        securityService.requireAdmin(request);
        return paymentService.updatePaymentStatus(paymentId, status);
    }
}