package com.example.smartshopapi.service.impl;

import com.example.smartshopapi.dto.PaymentRequestDTO;
import com.example.smartshopapi.dto.PaymentResponseDTO;
import com.example.smartshopapi.entity.Order;
import com.example.smartshopapi.entity.Payment;
import com.example.smartshopapi.enums.PaymentStatus;
import com.example.smartshopapi.enums.PaymentType;
import com.example.smartshopapi.mapper.PaymentMapper;
import com.example.smartshopapi.repository.OrderRepository;
import com.example.smartshopapi.repository.PaymentRepository;
import com.example.smartshopapi.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;
    
    @Override
    public PaymentResponseDTO addPayment(PaymentRequestDTO paymentRequest) {
        Order order = orderRepository.findById(paymentRequest.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + paymentRequest.getOrderId()));
        
        if (order.getStatus().name().equals("CONFIRMED")) {
            throw new RuntimeException("Cannot add payment to confirmed order");
        }
        
        if (paymentRequest.getAmount().compareTo(order.getRemainingAmount()) > 0) {
            throw new RuntimeException("Payment amount exceeds remaining amount due");
        }
        
        if (paymentRequest.getPaymentType() == PaymentType.CASH && 
            paymentRequest.getAmount().compareTo(new BigDecimal("20000")) > 0) {
            throw new RuntimeException("Cash payment limited to 20,000 DH maximum");
        }
        
        Payment payment = paymentMapper.toEntity(paymentRequest);
        payment.setOrder(order);
        
        Integer maxPaymentNumber = paymentRepository.findMaxPaymentNumberByOrderId(order.getId());
        payment.setPaymentNumber(maxPaymentNumber == null ? 1 : maxPaymentNumber + 1);
        
        if (paymentRequest.getPaymentType() == PaymentType.CASH) {
            payment.setStatus(PaymentStatus.ENCASHED);
            payment.setEncashmentDate(LocalDateTime.now());
        } else {
            payment.setStatus(PaymentStatus.PENDING);
        }
        
        Payment savedPayment = paymentRepository.save(payment);
        
        updateOrderRemainingAmount(order);
        
        return paymentMapper.toResponseDTO(savedPayment);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderIdOrderByPaymentNumber(orderId)
                .stream()
                .map(paymentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public PaymentResponseDTO updatePaymentStatus(Long paymentId, PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + paymentId));
        
        if (payment.getStatus() == PaymentStatus.ENCASHED) {
            throw new RuntimeException("Cannot modify already encashed payment");
        }
        
        payment.setStatus(status);
        if (status == PaymentStatus.ENCASHED) {
            payment.setEncashmentDate(LocalDateTime.now());
        }
        
        Payment updatedPayment = paymentRepository.save(payment);
        
        updateOrderRemainingAmount(payment.getOrder());
        
        return paymentMapper.toResponseDTO(updatedPayment);
    }
    
    private void updateOrderRemainingAmount(Order order) {
        BigDecimal totalEncashed = paymentRepository.sumEncaissedPaymentsByOrderId(order.getId());
        BigDecimal remainingAmount = order.getTotalAmount().subtract(totalEncashed);
        order.setRemainingAmount(remainingAmount.max(BigDecimal.ZERO));
        orderRepository.save(order);
    }
}