package com.example.smartshopapi.controller;

import com.example.smartshopapi.dto.LoginRequestDTO;
import com.example.smartshopapi.dto.UserResponseDTO;
import com.example.smartshopapi.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    
    @PostMapping("/login")
    public UserResponseDTO login(@RequestBody LoginRequestDTO loginRequest, HttpServletRequest request) {
        return authService.login(loginRequest, request);
    }
    
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest request) {
        authService.logout(request);
    }
    
    @GetMapping("/me")
    public UserResponseDTO getCurrentUser(HttpServletRequest request) {
        return authService.getCurrentUser(request);
    }
}