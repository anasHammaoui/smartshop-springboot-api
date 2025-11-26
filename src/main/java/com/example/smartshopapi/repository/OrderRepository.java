package com.example.smartshopapi.repository;

import com.example.smartshopapi.entity.Order;
import com.example.smartshopapi.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    Page<Order> findByClientId(Long clientId, Pageable pageable);
    @Query("SELECT COUNT(o) FROM Order o WHERE o.client.id = :clientId AND o.status = 'CONFIRMED'")
    Integer countConfirmedOrdersByClientId(Long clientId);
    
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.client.id = :clientId AND o.status = 'CONFIRMED'")
    BigDecimal sumTotalSpentByClientId(Long clientId);
}