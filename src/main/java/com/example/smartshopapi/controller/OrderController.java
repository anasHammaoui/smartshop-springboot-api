package com.example.smartshopapi.controller;

import com.example.smartshopapi.dto.OrderRequestDTO;
import com.example.smartshopapi.dto.OrderResponseDTO;
import com.example.smartshopapi.enums.OrderStatus;
import com.example.smartshopapi.service.OrderService;
import com.example.smartshopapi.service.SecurityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    private final SecurityService securityService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDTO createOrder(@Valid @RequestBody OrderRequestDTO orderRequest, HttpServletRequest request) {
        securityService.requireAdmin(request);
        return orderService.createOrder(orderRequest);
    }
    
    @GetMapping("/{id}")
    public OrderResponseDTO getOrder(@PathVariable Long id, HttpServletRequest request) {
        securityService.requireAdmin(request);
        return orderService.getOrderById(id);
    }
    
    @GetMapping
    public Page<OrderResponseDTO> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        securityService.requireAdmin(request);
        Pageable pageable = PageRequest.of(page, size);
        return orderService.getAllOrders(pageable);
    }
    
    @GetMapping("/client/{clientId}")
    public Page<OrderResponseDTO> getOrdersByClient(
            @PathVariable Long clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        securityService.requireAdminOrSameClient(request, clientId);
        Pageable pageable = PageRequest.of(page, size);
        return orderService.getOrdersByClientId(clientId, pageable);
    }
    
    @PutMapping("/{id}/status")
    public OrderResponseDTO updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status,
            HttpServletRequest request) {
        securityService.requireAdmin(request);
        return orderService.updateOrderStatus(id, status);
    }
}