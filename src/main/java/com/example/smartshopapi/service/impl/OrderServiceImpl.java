package com.example.smartshopapi.service.impl;

import com.example.smartshopapi.dto.OrderItemRequestDTO;
import com.example.smartshopapi.dto.OrderRequestDTO;
import com.example.smartshopapi.dto.OrderResponseDTO;
import com.example.smartshopapi.entity.*;
import com.example.smartshopapi.enums.OrderStatus;
import com.example.smartshopapi.mapper.OrderMapper;
import com.example.smartshopapi.repository.*;
import com.example.smartshopapi.service.FidelityService;
import com.example.smartshopapi.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final FidelityService fidelityService;
    
    @Value("${app.tva.rate:0.20}")
    private BigDecimal tvaRate;
    
    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequest) {
        Client client = clientRepository.findById(orderRequest.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + orderRequest.getClientId()));
        Order order = Order.builder()
                .client(client)
                .orderItems(new ArrayList<>())
                .build();
        

        BigDecimal subtotal = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (OrderItemRequestDTO itemRequest : orderRequest.getItems()) {
            Product product = productRepository.findActiveById(itemRequest.getProductId());
            if (product == null) {
                throw new RuntimeException("Product not found with ID: " + itemRequest.getProductId());
            }
            

            if (product.getStock() < itemRequest.getQuantity()) {
                order.setStatus(OrderStatus.REJECTED);
                orderRepository.save(order);
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            

            BigDecimal lineTotal = product.getUnitPrice()
                    .multiply(new BigDecimal(itemRequest.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);
            
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(product.getUnitPrice())
                    .lineTotal(lineTotal)
                    .build();
            
            orderItems.add(orderItem);
            subtotal = subtotal.add(lineTotal);
            

            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);
        }
        
        order.setOrderItems(orderItems);
        order.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));
        

        BigDecimal discountAmount = BigDecimal.ZERO;
        

        BigDecimal loyaltyDiscount = fidelityService.calculateDiscount(client.getTier(), subtotal);
        discountAmount = discountAmount.add(loyaltyDiscount);
        

        if (orderRequest.getPromoCode() != null && isValidPromoCode(orderRequest.getPromoCode())) {
            BigDecimal promoDiscount = subtotal.multiply(new BigDecimal("0.05"));
            discountAmount = discountAmount.add(promoDiscount);
            order.setPromoCode(orderRequest.getPromoCode());
        }
        
        order.setDiscountAmount(discountAmount.setScale(2, RoundingMode.HALF_UP));
        

        BigDecimal amountAfterDiscount = subtotal.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);
        BigDecimal tax = amountAfterDiscount.multiply(tvaRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalAmount = amountAfterDiscount.add(tax).setScale(2, RoundingMode.HALF_UP);
        
        order.setAmountAfterDiscount(amountAfterDiscount);
        order.setTax(tax);
        order.setTotalAmount(totalAmount);
        order.setRemainingAmount(totalAmount);
        
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponseDTO(savedOrder);
    }
    
    @Override
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
        return orderMapper.toResponseDTO(order);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toResponseDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDTO> getOrdersByClientId(Long clientId, Pageable pageable) {
        return orderRepository.findByClientId(clientId, pageable)
                .map(orderMapper::toResponseDTO);
    }
    
    @Override
    public OrderResponseDTO updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
        

        if (order.getStatus() == OrderStatus.CONFIRMED || 
            order.getStatus() == OrderStatus.REJECTED || 
            order.getStatus() == OrderStatus.CANCELED) {
            throw new RuntimeException("Cannot modify order with status: " + order.getStatus());
        }
        
        if (status == OrderStatus.CONFIRMED) {

            if (order.getRemainingAmount().compareTo(BigDecimal.ZERO) > 0) {
                throw new RuntimeException("Order must be fully paid before confirmation");
            }
            

            Client client = order.getClient();
            if (client.getFirstOrderDate() == null) {
                client.setFirstOrderDate(LocalDateTime.now());
            }
            client.setLastOrderDate(LocalDateTime.now());
            
            fidelityService.updateClientTier(client);
            clientRepository.save(client);
        }
        
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toResponseDTO(updatedOrder);
    }
    
    private boolean isValidPromoCode(String promoCode) {

        return promoCode != null && promoCode.matches("PROMO-[A-Z0-9]{4}");
    }
}