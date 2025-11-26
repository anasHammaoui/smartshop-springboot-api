package com.example.smartshopapi.service.impl;

import com.example.smartshopapi.entity.User;
import com.example.smartshopapi.enums.UserRole;
import com.example.smartshopapi.service.SecurityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {
    
    @Override
    public User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new RuntimeException("Not authenticated");
        }
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("Not authenticated");
        }
        
        return user;
    }
    
    @Override
    public void requireAdmin(HttpServletRequest request) {
        User user = getCurrentUser(request);
        if (user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Access denied. Admin role required");
        }
    }
}