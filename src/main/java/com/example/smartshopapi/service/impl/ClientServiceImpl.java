package com.example.smartshopapi.service.impl;

import com.example.smartshopapi.dto.ClientRequestDTO;
import com.example.smartshopapi.dto.ClientResponseDTO;
import com.example.smartshopapi.entity.Client;
import com.example.smartshopapi.entity.User;
import com.example.smartshopapi.enums.CustomerTier;
import com.example.smartshopapi.enums.UserRole;
import com.example.smartshopapi.mapper.ClientMapper;
import com.example.smartshopapi.repository.ClientRepository;
import com.example.smartshopapi.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;
    
    @Override
    public ClientResponseDTO createClient(ClientRequestDTO clientRequest) {
        if (clientRepository.existsByEmail(clientRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        if (userRepository.findByUsername(clientRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        
        // Create User first
        User user = User.builder()
                .username(clientRequest.getUsername())
                .password(clientRequest.getPassword())
                .role(UserRole.CLIENT)
                .build();
        User savedUser = userRepository.save(user);
        
        // Create Client with User reference
        Client client = Client.builder()
                .user(savedUser)
                .name(clientRequest.getName())
                .email(clientRequest.getEmail())
                .tier(CustomerTier.BASIC)
                .totalOrders(0)
                .totalSpent(BigDecimal.ZERO)
                .build();
        
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
        
        // Update User if username or password changed
        User user = client.getUser();
        if (!user.getUsername().equals(clientRequest.getUsername())) {
            if (userRepository.findByUsername(clientRequest.getUsername()).isPresent()) {
                throw new RuntimeException("Username already exists");
            }
            user.setUsername(clientRequest.getUsername());
        }
        
        if (!user.getPassword().equals(clientRequest.getPassword())) {
            user.setPassword(clientRequest.getPassword());
        }
        
        userRepository.save(user);
        
        // Update Client
        client.setName(clientRequest.getName());
        client.setEmail(clientRequest.getEmail());
        
        Client updatedClient = clientRepository.save(client);
        return clientMapper.toResponseDTO(updatedClient);
    }
    
    @Override
    public void deleteClient(Long id) {
        Client client = findClientEntity(id);
        User user = client.getUser();
        
        // Delete client first (due to foreign key constraint)
        clientRepository.delete(client);
        
        // Then delete the associated user
        if (user != null) {
            userRepository.delete(user);
        }
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

    @Override
    @Transactional(readOnly = true)
    public Client findClientByUserId(Long userId) {
        return clientRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Client not found for user id: " + userId));
    }
}