package com.example.smartshopapi.service.impl;

import com.example.smartshopapi.entity.Client;
import com.example.smartshopapi.enums.CustomerTier;
import com.example.smartshopapi.repository.OrderRepository;
import com.example.smartshopapi.service.FidelityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class FidelityServiceImpl implements FidelityService {
    
    private final OrderRepository orderRepository;
    
    @Override
    public CustomerTier calculateTier(Integer totalOrders, BigDecimal totalSpent) {
        if (totalOrders >= 20 || totalSpent.compareTo(new BigDecimal("15000")) >= 0) {
            return CustomerTier.PLATINUM;
        }
        if (totalOrders >= 10 || totalSpent.compareTo(new BigDecimal("5000")) >= 0) {
            return CustomerTier.GOLD;
        }
        if (totalOrders >= 3 || totalSpent.compareTo(new BigDecimal("1000")) >= 0) {
            return CustomerTier.SILVER;
        }
        return CustomerTier.BASIC;
    }
    
    @Override
    public BigDecimal calculateDiscount(CustomerTier tier, BigDecimal sousTotal) {
        switch (tier) {
            case SILVER:
                if (sousTotal.compareTo(new BigDecimal("500")) >= 0) {
                    return sousTotal.multiply(new BigDecimal("0.05"));
                }
                break;
            case GOLD:
                if (sousTotal.compareTo(new BigDecimal("800")) >= 0) {
                    return sousTotal.multiply(new BigDecimal("0.10"));
                }
                break;
            case PLATINUM:
                if (sousTotal.compareTo(new BigDecimal("1200")) >= 0) {
                    return sousTotal.multiply(new BigDecimal("0.15"));
                }
                break;
            default:
                break;
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public void updateClientTier(Client client) {
        Integer confirmedOrders = orderRepository.countConfirmedOrdersByClientId(client.getId());
        BigDecimal totalSpent = orderRepository.sumTotalSpentByClientId(client.getId());
        
        client.setTotalOrders(confirmedOrders);
        client.setTotalSpent(totalSpent);
        client.setTier(calculateTier(confirmedOrders, totalSpent));
    }
}