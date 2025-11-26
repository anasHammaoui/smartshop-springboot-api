package com.example.smartshopapi.service;

import com.example.smartshopapi.dto.LoginRequestDTO;
import com.example.smartshopapi.dto.UserResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    UserResponseDTO login(LoginRequestDTO loginRequest, HttpServletRequest request);
    String logout(HttpServletRequest request);
    UserResponseDTO getCurrentUser(HttpServletRequest request);
}