# SmartShop API - Loyalty Management System

## Overview
SmartShop is a REST API for commercial management designed for MicroTech Morocco, a B2B IT equipment distributor based in Casablanca. The application manages a portfolio of 650 active clients with a progressive loyalty discount system and multi-payment fractional payments per invoice.

## Features

### Client Management
- Create, read, update, delete clients
- Automatic loyalty tier calculation (BASIC, SILVER, GOLD, PLATINUM)
- Order history and statistics tracking

### Product Management
- Product CRUD operations with soft delete
- Stock management
- Price management

### Order Management
- Multi-product orders with quantities
- Automatic discount calculation based on loyalty tier
- Promo code support (PROMO-XXXX format)
- Stock validation and automatic updates
- Tax calculation (20% configurable)

### Payment System
- Multi-payment support per order
- Three payment types: CASH, CHECK, TRANSFER
- Payment status tracking (PENDING, ENCASHED, REJECTED)
- Legal cash limit validation (20,000 DH)
- Sequential payment numbering

### Loyalty System
- Automatic tier calculation based on:
  - SILVER: 3+ orders OR 1,000+ DH spent
  - GOLD: 10+ orders OR 5,000+ DH spent  
  - PLATINUM: 20+ orders OR 15,000+ DH spent
- Progressive discounts:
  - SILVER: 5% if subtotal ≥ 500 DH
  - GOLD: 10% if subtotal ≥ 800 DH
  - PLATINUM: 15% if subtotal ≥ 1,200 DH

## Technology Stack
- Java 17
- Spring Boot 4.0.0
- Spring Data JPA
- MySQL Database
- MapStruct for mapping
- Lombok for boilerplate reduction
- Bean Validation

## Getting Started

### Prerequisites
- Java 17+
- MySQL 8.0+
- Maven 3.6+

### Database Setup
1. Create MySQL database named `smartshop`
2. Update `application.properties` with your database credentials

### Running the Application
```bash
mvn spring-boot:run
```

The application will start on port 8080.

### Default Users
- Admin: username=`admin`, password=`admin123`
- Client: username=`client`, password=`client123`

## API Endpoints

### Authentication
- POST `/api/auth/login` - Login
- POST `/api/auth/logout` - Logout

### Clients
- POST `/api/clients` - Create client (ADMIN)
- GET `/api/clients/{id}` - Get client (ADMIN)
- PUT `/api/clients/{id}` - Update client (ADMIN)
- DELETE `/api/clients/{id}` - Delete client (ADMIN)
- GET `/api/clients` - List clients (ADMIN)
- GET `/api/clients/{id}/profile` - Get client profile (ADMIN/CLIENT)

### Products
- POST `/api/products` - Create product (ADMIN)
- GET `/api/products/{id}` - Get product
- PUT `/api/products/{id}` - Update product (ADMIN)
- DELETE `/api/products/{id}` - Soft delete product (ADMIN)
- GET `/api/products` - List products

### Orders
- POST `/api/orders` - Create order (ADMIN)
- GET `/api/orders/{id}` - Get order (ADMIN)
- GET `/api/orders` - List orders (ADMIN)
- GET `/api/orders/client/{clientId}` - Get client orders (ADMIN/CLIENT)
- PUT `/api/orders/{id}/status` - Update order status (ADMIN)

### Payments
- POST `/api/payments` - Add payment (ADMIN)
- GET `/api/payments/order/{orderId}` - Get order payments (ADMIN)
- PUT `/api/payments/{paymentId}/status` - Update payment status (ADMIN)

## Business Rules

### Order Status Flow
- PENDING → CONFIRMED (when fully paid)
- PENDING → CANCELED (manual by admin)
- PENDING → REJECTED (insufficient stock)

### Payment Rules
- Cash payments: max 20,000 DH, immediately encashed
- Check/Transfer payments: require manual encashment
- Orders must be fully paid before confirmation

### Loyalty Calculation
- Tier calculated after each confirmed order
- Discounts applied to future orders based on current tier
- Statistics updated automatically

## Testing
Use Postman or any API testing tool to test the endpoints. Sample data is automatically created on startup.

## Architecture
The application follows a layered architecture:
- **Controller Layer**: REST endpoints
- **Service Layer**: Business logic
- **Repository Layer**: Data access
- **Entity Layer**: JPA entities
- **DTO Layer**: Data transfer objects
- **Mapper Layer**: Entity-DTO conversion

## Error Handling
Centralized exception handling with appropriate HTTP status codes:
- 400: Validation errors
- 401: Not authenticated
- 403: Access denied
- 404: Resource not found
- 422: Business rule violation
- 500: Internal server error