package com.example.smartshopapi.service.impl;

import com.example.smartshopapi.dto.ClientRequestDTO;
import com.example.smartshopapi.dto.ClientResponseDTO;
import com.example.smartshopapi.entity.Client;
import com.example.smartshopapi.enums.CustomerTier;
import com.example.smartshopapi.mapper.ClientMapper;
import com.example.smartshopapi.repository.ClientRepository;
import com.example.smartshopapi.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements ClientService {
    
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    
    @Override
    public ClientResponseDTO createClient(ClientRequestDTO clientRequest) {
        if (clientRepository.existsByEmail(clientRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        Client client = clientMapper.toEntity(clientRequest);
        Client savedClient = clientRepository.save(client);
        return clientMapper.toResponseDTO(savedClient);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ClientResponseDTO getClientById(Long id) {
        Client client = findClientEntity(id);
        return clientMapper.toResponseDTO(client);
    }
    
    @Override
    public ClientResponseDTO updateClient(Long id, ClientRequestDTO clientRequest) {
        Client client = findClientEntity(id);
        
        if (!client.getEmail().equals(clientRequest.getEmail()) && 
            clientRepository.existsByEmail(clientRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        clientMapper.updateEntityFromDTO(clientRequest, client);
        Client updatedClient = clientRepository.save(client);
        return clientMapper.toResponseDTO(updatedClient);
    }
    
    @Override
    public void deleteClient(Long id) {
        Client client = findClientEntity(id);
        clientRepository.delete(client);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ClientResponseDTO> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable)
                .map(clientMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Client findClientEntity(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
    }

    

}