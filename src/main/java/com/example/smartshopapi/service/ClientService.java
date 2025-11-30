package com.example.smartshopapi.service;

import com.example.smartshopapi.dto.ClientRequestDTO;
import com.example.smartshopapi.dto.ClientResponseDTO;
import com.example.smartshopapi.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ClientService {
    ClientResponseDTO createClient(ClientRequestDTO clientRequest);
    ClientResponseDTO getClientById(Long id);
    ClientResponseDTO updateClient(Long id, ClientRequestDTO clientRequest);
    void deleteClient(Long id);
    Page<ClientResponseDTO> getAllClients(Pageable pageable);
    Client findClientEntity(Long id);
    Client findClientByUserId(Long userId);
}