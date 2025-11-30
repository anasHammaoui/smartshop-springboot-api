package com.example.smartshopapi.service.impl;

import com.example.smartshopapi.entity.Client;
import com.example.smartshopapi.entity.User;
import com.example.smartshopapi.enums.UserRole;
import com.example.smartshopapi.repository.ClientRepository;
import com.example.smartshopapi.service.SecurityService;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {
    
    private final ClientRepository clientRepository;
    
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
            // Find the client associated with this user
            Client userClient = clientRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Client profile not found for user"));
            
            // Check if the requested clientId matches the user's client
            if (!userClient.getId().equals(clientId)) {
                throw new RuntimeException("Access denied. You can only access your own data");
            }
            return;
        }
        
        throw new RuntimeException("Access denied");
    }
}