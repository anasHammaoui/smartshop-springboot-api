package com.example.smartshopapi.mapper;

import com.example.smartshopapi.dto.PaymentRequestDTO;
import com.example.smartshopapi.dto.PaymentResponseDTO;
import com.example.smartshopapi.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "paymentNumber", ignore = true)
    @Mapping(target = "encashmentDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Payment toEntity(PaymentRequestDTO dto);
    
    @Mapping(source = "order.id", target = "orderId")
    PaymentResponseDTO toResponseDTO(Payment entity);
}