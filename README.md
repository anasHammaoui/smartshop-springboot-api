# SmartShop API - Commercial Management System

## Project Context
SmartShop is a commercial management web application designed for **MicroTech Morocco**, a B2B IT equipment distributor based in Casablanca. The application manages a portfolio of **650 active clients** with a progressive loyalty discount system and multi-payment fractional payments per invoice. The system ensures complete traceability of all financial events through an immutable history and optimizes cash flow management.

## Important Notes
- **Pure Backend REST API** (API only, no frontend)
- **No graphical interface**
- **Testing and demonstrations** via API tester (Postman or Swagger)
- **HTTP Session authentication** (login/logout)
- **No JWT, No Spring Security**
- **Role management**: ADMIN (MicroTech employee using SmartShop) and CLIENT (client companies purchasing from MicroTech)

## Overview
SmartShop is a comprehensive REST API for commercial management that handles the complete business workflow from client registration to order fulfillment and payment processing, with an integrated loyalty system that rewards customers based on their purchase history.

## Functional Requirements

### 1. Client Management
- **Create a client** with name and email
- **View client information** including loyalty tier and statistics
- **Update client data** (personal information)
- **Delete a client** from the system
- **Automatic tracking** of:
  - Statistics: Total number of orders + Cumulative amount (total of confirmed orders)
  - Date of first and last order
  - Order history displaying order ID, creation date, total amount, and status

### 2. Automatic Loyalty System
- **Automatic tier calculation** based on client history:
  - **BASIC**: Default client (0 orders)
  - **SILVER**: From 3 orders OR 1,000 DH cumulative
  - **GOLD**: From 10 orders OR 5,000 DH cumulative
  - **PLATINUM**: From 20 orders OR 15,000 DH cumulative
- **Tier update** after each confirmed order
- **Discount application** according to current tier:
  - **SILVER**: 5% if subtotal ≥ 500 DH
  - **GOLD**: 10% if subtotal ≥ 800 DH
  - **PLATINUM**: 15% if subtotal ≥ 1,200 DH

### 3. Product Management
- **Add products** with name, price, and stock
- **Modify product information**
- **Delete products** (soft delete if used in existing orders)
- **View product list** with filters and pagination

### 4. Order Management
- **Create multi-product orders** with quantities
- **Validate prerequisites**: Stock availability for each product
- **Apply cumulative discounts**: Loyalty discount + Promo code PROMO-XXXX (+5%)
- **Automatic calculations**: Subtotal, discounts, VAT (20%), total
- **Status management**: PENDING, CONFIRMED, CANCELED, REJECTED

### 5. Multi-Payment System
- **Three payment types**: CASH (max 20,000 DH), CHECK, TRANSFER
- **Payment status tracking**: PENDING, ENCASHED, REJECTED
- **Fractional payments**: Orders can be paid in multiple installments
- **Validation rule**: Order must be fully paid before confirmation

## Technical Requirements

### Architecture & Technology Stack
- **Application Type**: Backend REST API only (no frontend)
- **Testing and Simulation**: Via Postman or Swagger
- **Framework**: Spring Boot 4.0.0
- **Java**: Version 17
- **API**: REST with JSON
- **Database**: MySQL
- **ORM**: Spring Data JPA/Hibernate
- **Unit Testing**: JUnit, Mockito
- **Architecture**: Layered (Controller, Service, Repository, Entity, DTO)
- **Data Validation**: Annotations
- **Utilities**: Lombok, MapStruct, Builder Pattern
- **Authentication**: HTTP Session (no JWT, no Spring Security)

### Data Model
- **User**: id, username, password, role (ADMIN/CLIENT)
- **Client**: id, name, email, loyalty tier, statistics
- **Product**: id, name, unit price, available stock
- **Order**: id, client, items list, date, subtotal, discount, tax, total, promo code, status, remaining amount
- **OrderItem**: id, product, quantity, unit price, line total
- **Payment**: id, order_id, payment_number, amount, payment_type, payment_date, encashment_date

### System Enums
- **UserRole**: ADMIN (MicroTech employee), CLIENT (client company)
- **CustomerTier**: BASIC, SILVER, GOLD, PLATINUM
- **OrderStatus**: PENDING, CONFIRMED, CANCELED, REJECTED
- **PaymentStatus**: PENDING, ENCASHED, REJECTED
- **PaymentType**: CASH, CHECK, TRANSFER

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

## Critical Business Rules

### Stock Validation
- Stock check: requested_quantity ≤ available_stock
- Rounding: all amounts to 2 decimal places
- Promo codes: strict format PROMO-XXXX, single use possible
- VAT rate: 20% by default (configurable)

### Order Status Transitions
**Automatic (system-managed):**
- PENDING → REJECTED: if insufficient stock during order creation

**Manual (API endpoint - ADMIN only):**
- PENDING → CONFIRMED: validation by ADMIN (after full payment verification)
- PENDING → CANCELED: cancellation by ADMIN
- Final statuses: CONFIRMED, REJECTED, CANCELED (no further modifications)

### Permission Matrix
**CLIENT can only:**
- Login and view OWN data (profile, loyalty tier, order history, statistics)
- View product list (read-only)
- CANNOT create, modify, delete anything
- CANNOT see other clients' data

**ADMIN can do everything:**
- All CRUD operations
- View all clients
- Create orders for any client
- Record payments
- Validate, cancel, reject orders

### Payment Rules
- Cash payments: max 20,000 DH, immediately encashed
- Check/Transfer payments: require manual encashment
- Orders must be fully paid before confirmation
- Sequential payment numbering per order

### Loyalty System Rules
- Tier calculated after each confirmed order
- Discounts applied to future orders based on current tier
- Statistics updated automatically
- VAT calculated on amount AFTER discount (Morocco standard)

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
Centralized exception handling with @ControllerAdvice and appropriate HTTP status codes:
- **400**: Validation errors
- **401**: Not authenticated
- **403**: Access denied (insufficient permissions)
- **404**: Resource not found
- **422**: Business rule violation (insufficient stock, order already validated, etc.)
- **500**: Internal server error

Each JSON error response includes:
- Timestamp
- HTTP code
- Error type
- Explanatory message
- Request path