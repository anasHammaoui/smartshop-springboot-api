package com.example.smartshopapi.controller;

import com.example.smartshopapi.dto.ClientRequestDTO;
import com.example.smartshopapi.dto.ClientResponseDTO;
import com.example.smartshopapi.service.ClientService;
import com.example.smartshopapi.service.SecurityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {
    
    private final ClientService clientService;
    private final SecurityService securityService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientResponseDTO createClient(@Valid @RequestBody ClientRequestDTO clientRequest, HttpServletRequest request) {
        securityService.requireAdmin(request);
        return clientService.createClient(clientRequest);
    }
    
    @GetMapping("/{id}")
    public ClientResponseDTO getClient(@PathVariable Long id, HttpServletRequest request) {
        securityService.requireAdmin(request);
        return clientService.getClientById(id);
    }
    
    @PutMapping("/{id}")
    public ClientResponseDTO updateClient(@PathVariable Long id, @Valid @RequestBody ClientRequestDTO clientRequest, HttpServletRequest request) {
        securityService.requireAdmin(request);
        return clientService.updateClient(id, clientRequest);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@PathVariable Long id, HttpServletRequest request) {
        securityService.requireAdmin(request);
        clientService.deleteClient(id);
    }
    
    @GetMapping
    public Page<ClientResponseDTO> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        securityService.requireAdmin(request);
        Pageable pageable = PageRequest.of(page, size);
        return clientService.getAllClients(pageable);
    }
    
    @GetMapping("/{id}/profile")
    public ClientResponseDTO getClientProfile(@PathVariable Long id, HttpServletRequest request) {
        securityService.requireAdminOrSameClient(request, id);
        return clientService.getClientById(id);
    }
}