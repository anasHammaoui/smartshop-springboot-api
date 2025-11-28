package com.example.smartshopapi.service;

import com.example.smartshopapi.dto.OrderRequestDTO;
import com.example.smartshopapi.dto.OrderResponseDTO;
import com.example.smartshopapi.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO orderRequest);
    OrderResponseDTO getOrderById(Long id);
    Page<OrderResponseDTO> getAllOrders(Pageable pageable);
    Page<OrderResponseDTO> getOrdersByClientId(Long clientId, Pageable pageable);
    OrderResponseDTO updateOrderStatus(Long id, OrderStatus status);
}