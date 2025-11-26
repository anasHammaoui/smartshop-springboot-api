package com.example.smartshopapi.service.impl;

import com.example.smartshopapi.dto.LoginRequestDTO;
import com.example.smartshopapi.dto.UserResponseDTO;
import com.example.smartshopapi.entity.User;
import com.example.smartshopapi.repository.UserRepository;
import com.example.smartshopapi.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final UserRepository userRepository;
    
    @Override
    public UserResponseDTO login(LoginRequestDTO loginRequest, HttpServletRequest request) {
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElse(null);
        
        if (user == null || !user.getPassword().equals(loginRequest.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
    
    @Override
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "Logged out successfully";
    }
    
    @Override
    public UserResponseDTO getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new RuntimeException("Not logged in");
        }
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("Not logged in");
        }
        
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}