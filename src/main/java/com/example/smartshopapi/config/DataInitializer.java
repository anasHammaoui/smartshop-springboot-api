package com.example.smartshopapi.config;

import com.example.smartshopapi.entity.*;
import com.example.smartshopapi.enums.*;
import com.example.smartshopapi.repository.*;
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
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    
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
            
            Client client4 = Client.builder()
                    .name("MegaTech Industries")
                    .email("orders@megatech.ma")
                    .tier(CustomerTier.PLATINUM)
                    .totalOrders(25)
                    .totalSpent(new BigDecimal("18500.00"))
                    .build();
            clientRepository.save(client4);
            
            Client client5 = Client.builder()
                    .name("StartupHub")
                    .email("tech@startuphub.ma")
                    .tier(CustomerTier.BASIC)
                    .totalOrders(0)
                    .totalSpent(BigDecimal.ZERO)
                    .build();
            clientRepository.save(client5);
            
            System.out.println("Sample clients created (5 records)");
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
            
            System.out.println("Sample products created (5 records)");
        }
        
        if (orderRepository.count() == 0) {
            Client client1 = clientRepository.findById(1L).orElse(null);
            Product product1 = productRepository.findById(1L).orElse(null);
            
            if (client1 != null && product1 != null) {
                for (int i = 1; i <= 5; i++) {
                    Order order = Order.builder()
                            .client(client1)
                            .subtotal(new BigDecimal("1000.00"))
                            .discountAmount(new BigDecimal("50.00"))
                            .amountAfterDiscount(new BigDecimal("950.00"))
                            .tax(new BigDecimal("190.00"))
                            .totalAmount(new BigDecimal("1140.00"))
                            .remainingAmount(BigDecimal.ZERO)
                            .status(OrderStatus.CONFIRMED)
                            .build();
                    orderRepository.save(order);
                }
                System.out.println("Sample orders created (5 records)");
            }
        }
        
        if (paymentRepository.count() == 0) {
            Order order1 = orderRepository.findById(1L).orElse(null);
            if (order1 != null) {
                for (int i = 1; i <= 5; i++) {
                    Payment payment = Payment.builder()
                            .order(order1)
                            .paymentNumber(i)
                            .amount(new BigDecimal("228.00"))
                            .paymentType(PaymentType.CASH)
                            .paymentDate(java.time.LocalDateTime.now())
                            .status(PaymentStatus.ENCASHED)
                            .encashmentDate(java.time.LocalDateTime.now())
                            .build();
                    paymentRepository.save(payment);
                }
                System.out.println("Sample payments created (5 records)");
            }
        }
    }
}