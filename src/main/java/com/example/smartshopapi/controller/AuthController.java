package com.example.smartshopapi.controller;

import com.example.smartshopapi.dto.LoginRequestDTO;
import com.example.smartshopapi.dto.UserResponseDTO;
import com.example.smartshopapi.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequestDTO loginRequest, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            UserResponseDTO user = authService.login(loginRequest, request);
            response.put("success", true);
            response.put("user", user);
            response.put("message", "Login successful");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }
    
    @PostMapping("/logout")
    public Map<String, Object> logout(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            authService.logout(request);
            response.put("success", true);
            response.put("message", "Logged out successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }
    
    @GetMapping("/me")
    public Map<String, Object> getCurrentUser(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            UserResponseDTO user = authService.getCurrentUser(request);
            response.put("success", true);
            response.put("user", user);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }
}