package com.example.smartshopapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequestDTO {
    @NotBlank(message = "Name is required")
    private String nom;
    
    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    private String email;
}