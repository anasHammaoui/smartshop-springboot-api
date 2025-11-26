package com.example.smartshopapi.repository;

import com.example.smartshopapi.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    List<Payment> findByOrderIdOrderByPaymentNumber(Long orderId);
    
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.order.id = :orderId AND p.status = 'ENCASHED'")
    BigDecimal sumEncaissedPaymentsByOrderId(Long orderId);
    
    @Query("SELECT MAX(p.paymentNumber) FROM Payment p WHERE p.order.id = :orderId")
    Integer findMaxPaymentNumberByOrderId(Long orderId);
}