package com.example.smartshopapi.mapper;

import com.example.smartshopapi.dto.OrderItemResponseDTO;
import com.example.smartshopapi.dto.OrderResponseDTO;
import com.example.smartshopapi.entity.Order;
import com.example.smartshopapi.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.name", target = "clientName")
    @Mapping(source = "orderItems", target = "items")
    OrderResponseDTO toResponseDTO(Order entity);
    
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    OrderItemResponseDTO toItemResponseDTO(OrderItem entity);
}