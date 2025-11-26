package com.example.smartshopapi.service;

import com.example.smartshopapi.entity.Client;
import com.example.smartshopapi.enums.CustomerTier;

import java.math.BigDecimal;

public interface FidelityService {
    CustomerTier calculateTier(Integer totalOrders, BigDecimal totalSpent);
    BigDecimal calculateDiscount(CustomerTier tier, BigDecimal sousTotal);
    void updateClientTier(Client client);
}