package com.example.smartshopapi.config;

import com.example.smartshopapi.entity.User;
import com.example.smartshopapi.enums.UserRole;
import com.example.smartshopapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Create default admin user
            User admin = User.builder()
                    .username("admin")
                    .password("admin123")
                    .role(UserRole.ADMIN)
                    .build();
            userRepository.save(admin);
            
            // Create default client user
            User client = User.builder()
                    .username("client")
                    .password("client123")
                    .role(UserRole.CLIENT)
                    .build();
            userRepository.save(client);
            
            System.out.println("Default users created:");
            System.out.println("Admin - username: admin, password: admin123");
            System.out.println("Client - username: client, password: client123");
        }
    }
}