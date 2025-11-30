package com.example.smartshopapi.service;

import com.example.smartshopapi.entity.Client;
import com.example.smartshopapi.enums.CustomerTier;
import com.example.smartshopapi.repository.OrderRepository;
import com.example.smartshopapi.service.impl.FidelityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FidelityServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private FidelityServiceImpl fidelityService;

    private Client client;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .id(1L)
                .name("Test Client")
                .email("test@example.com")
                .tier(CustomerTier.BASIC)
                .totalOrders(0)
                .totalSpent(BigDecimal.ZERO)
                .build();
    }

    @Test
    void calculateTier_ShouldReturnBasic_WhenNoOrdersAndNoSpending() {
        CustomerTier tier = fidelityService.calculateTier(0, BigDecimal.ZERO);
        assertEquals(CustomerTier.BASIC, tier);
    }

    @Test
    void calculateTier_ShouldReturnSilver_When3Orders() {
        CustomerTier tier = fidelityService.calculateTier(3, new BigDecimal("500"));
        assertEquals(CustomerTier.SILVER, tier);
    }

    @Test
    void calculateTier_ShouldReturnSilver_When1000Spent() {
        CustomerTier tier = fidelityService.calculateTier(1, new BigDecimal("1000"));
        assertEquals(CustomerTier.SILVER, tier);
    }

    @Test
    void calculateTier_ShouldReturnGold_When10Orders() {
        CustomerTier tier = fidelityService.calculateTier(10, new BigDecimal("2000"));
        assertEquals(CustomerTier.GOLD, tier);
    }

    @Test
    void calculateTier_ShouldReturnGold_When5000Spent() {
        CustomerTier tier = fidelityService.calculateTier(5, new BigDecimal("5000"));
        assertEquals(CustomerTier.GOLD, tier);
    }

    @Test
    void calculateTier_ShouldReturnPlatinum_When20Orders() {
        CustomerTier tier = fidelityService.calculateTier(20, new BigDecimal("8000"));
        assertEquals(CustomerTier.PLATINUM, tier);
    }

    @Test
    void calculateTier_ShouldReturnPlatinum_When15000Spent() {
        CustomerTier tier = fidelityService.calculateTier(8, new BigDecimal("15000"));
        assertEquals(CustomerTier.PLATINUM, tier);
    }

    @Test
    void calculateDiscount_ShouldReturnZero_ForBasicTier() {
        BigDecimal discount = fidelityService.calculateDiscount(CustomerTier.BASIC, new BigDecimal("1000"));
        assertEquals(BigDecimal.ZERO, discount);
    }

    @Test
    void calculateDiscount_ShouldReturn5Percent_ForSilverTierAbove500() {
        BigDecimal discount = fidelityService.calculateDiscount(CustomerTier.SILVER, new BigDecimal("600"));
        assertEquals(new BigDecimal("30.00"), discount);
    }

    @Test
    void calculateDiscount_ShouldReturnZero_ForSilverTierBelow500() {
        BigDecimal discount = fidelityService.calculateDiscount(CustomerTier.SILVER, new BigDecimal("400"));
        assertEquals(BigDecimal.ZERO, discount);
    }

    @Test
    void calculateDiscount_ShouldReturn10Percent_ForGoldTierAbove800() {
        BigDecimal discount = fidelityService.calculateDiscount(CustomerTier.GOLD, new BigDecimal("1000"));
        assertEquals(new BigDecimal("100.00"), discount);
    }

    @Test
    void calculateDiscount_ShouldReturn15Percent_ForPlatinumTierAbove1200() {
        BigDecimal discount = fidelityService.calculateDiscount(CustomerTier.PLATINUM, new BigDecimal("2000"));
        assertEquals(new BigDecimal("300.00"), discount);
    }

    @Test
    void updateClientTier_ShouldUpdateClientStatistics() {
        when(orderRepository.countConfirmedOrdersByClientId(1L)).thenReturn(5);
        when(orderRepository.sumTotalSpentByClientId(1L)).thenReturn(new BigDecimal("2500"));

        fidelityService.updateClientTier(client);

        assertEquals(5, client.getTotalOrders());
        assertEquals(new BigDecimal("2500"), client.getTotalSpent());
        assertEquals(CustomerTier.SILVER, client.getTier());
    }
}