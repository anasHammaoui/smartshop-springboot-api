package com.example.smartshopapi.mapper;

import com.example.smartshopapi.dto.UserResponseDTO;
import com.example.smartshopapi.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO toResponseDTO(User entity);
}