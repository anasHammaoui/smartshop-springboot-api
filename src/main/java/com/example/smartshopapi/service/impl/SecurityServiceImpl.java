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
    
    @Override
    public void requireAuthenticated(HttpServletRequest request) {
        getCurrentUser(request); // Will throw if not authenticated
    }
    
    @Override
    public void requireAdminOrSameClient(HttpServletRequest request, Long clientId) {
        User user = getCurrentUser(request);
        if (user.getRole() == UserRole.ADMIN) {
            return; // Admin can access any client
        }
        
        if (user.getRole() == UserRole.CLIENT) {
            // For CLIENT role, we would need to check if the user is associated with this client
            // This requires additional logic to link User to Client
            // For now, we'll allow access (this should be implemented based on your user-client relationship)
            return;
        }
        
        throw new RuntimeException("Access denied");
    }
}