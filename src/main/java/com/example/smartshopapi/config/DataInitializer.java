package com.example.smartshopapi.config;

import com.example.smartshopapi.entity.Client;
import com.example.smartshopapi.entity.Product;
import com.example.smartshopapi.entity.User;
import com.example.smartshopapi.enums.CustomerTier;
import com.example.smartshopapi.enums.UserRole;
import com.example.smartshopapi.repository.ClientRepository;
import com.example.smartshopapi.repository.ProductRepository;
import com.example.smartshopapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .username("admin")
                    .password("admin123")
                    .role(UserRole.ADMIN)
                    .build();
            userRepository.save(admin);
            
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
        
        if (clientRepository.count() == 0) {
            Client client1 = Client.builder()
                    .name("TechCorp SARL")
                    .email("contact@techcorp.ma")
                    .tier(CustomerTier.SILVER)
                    .totalOrders(5)
                    .totalSpent(new BigDecimal("2500.00"))
                    .build();
            clientRepository.save(client1);
            
            Client client2 = Client.builder()
                    .name("InfoSys Ltd")
                    .email("admin@infosys.ma")
                    .tier(CustomerTier.GOLD)
                    .totalOrders(12)
                    .totalSpent(new BigDecimal("8500.00"))
                    .build();
            clientRepository.save(client2);
            
            Client client3 = Client.builder()
                    .name("Digital Solutions")
                    .email("contact@digitalsol.ma")
                    .tier(CustomerTier.BASIC)
                    .totalOrders(1)
                    .totalSpent(new BigDecimal("450.00"))
                    .build();
            clientRepository.save(client3);
            
            System.out.println("Sample clients created");
        }
        
        if (productRepository.count() == 0) {
            Product product1 = Product.builder()
                    .name("Laptop Dell Inspiron 15")
                    .unitPrice(new BigDecimal("8500.00"))
                    .stock(25)
                    .build();
            productRepository.save(product1);
            
            Product product2 = Product.builder()
                    .name("HP Printer LaserJet Pro")
                    .unitPrice(new BigDecimal("2200.00"))
                    .stock(15)
                    .build();
            productRepository.save(product2);
            
            Product product3 = Product.builder()
                    .name("Wireless Mouse Logitech")
                    .unitPrice(new BigDecimal("150.00"))
                    .stock(50)
                    .build();
            productRepository.save(product3);
            
            Product product4 = Product.builder()
                    .name("External Hard Drive 1TB")
                    .unitPrice(new BigDecimal("650.00"))
                    .stock(30)
                    .build();
            productRepository.save(product4);
            
            Product product5 = Product.builder()
                    .name("Monitor Samsung 24 inch")
                    .unitPrice(new BigDecimal("1800.00"))
                    .stock(20)
                    .build();
            productRepository.save(product5);
            
            System.out.println("Sample products created");
        }
    }
}