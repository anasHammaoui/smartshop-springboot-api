package com.example.smartshopapi.service;

import com.example.smartshopapi.entity.User;
import com.example.smartshopapi.enums.UserRole;
import jakarta.servlet.http.HttpServletRequest;

public interface SecurityService {
    User getCurrentUser(HttpServletRequest request);
    void requireAdmin(HttpServletRequest request);
    void requireAuthenticated(HttpServletRequest request);
    void requireAdminOrSameClient(HttpServletRequest request, Long clientId);
}