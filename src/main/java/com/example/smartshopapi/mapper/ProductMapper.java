package com.example.smartshopapi.mapper;

import com.example.smartshopapi.dto.ProductRequestDTO;
import com.example.smartshopapi.dto.ProductResponseDTO;
import com.example.smartshopapi.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    
    Product toEntity(ProductRequestDTO dto);
    
    ProductResponseDTO toResponseDTO(Product entity);
    
    void updateEntityFromDTO(ProductRequestDTO dto, @MappingTarget Product entity);
}