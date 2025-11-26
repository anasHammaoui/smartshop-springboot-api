package com.example.smartshopapi.controller;

import com.example.smartshopapi.entity.User;
import com.example.smartshopapi.enums.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @GetMapping("/admin-only")
    public Map<String, Object> adminOnly(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.put("success", false);
            response.put("message", "Please login first");
            return response;
        }
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.put("success", false);
            response.put("message", "Please login first");
            return response;
        }
        
        if (user.getRole() != UserRole.ADMIN) {
            response.put("success", false);
            response.put("message", "Admin access required");
            return response;
        }
        
        response.put("success", true);
        response.put("message", "Welcome Admin: " + user.getUsername());
        return response;
    }
    
    @GetMapping("/client-only")
    public Map<String, Object> clientOnly(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.put("success", false);
            response.put("message", "Please login first");
            return response;
        }
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.put("success", false);
            response.put("message", "Please login first");
            return response;
        }
        
        if (user.getRole() != UserRole.CLIENT) {
            response.put("success", false);
            response.put("message", "Client access required");
            return response;
        }
        
        response.put("success", true);
        response.put("message", "Welcome Client: " + user.getUsername());
        return response;
    }
    
    @GetMapping("/hello")
    public String hello() {
        return "Hello from SmartShop API!";
    }
}