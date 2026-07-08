# 📱 PhoneStoreManagement

A Java console application for managing a phone store, built with **JDBC + PostgreSQL** following a clean 4-layer architecture.

---

## 🗂 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Database Schema](#database-schema)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Architecture](#architecture)

---

## Overview

PhoneStoreManagement is a command-line interface (CLI) application designed for store administrators to manage products, customers, invoices, and revenue statistics. All data is persisted in a PostgreSQL database via JDBC.

---

## Features

### 🔐 Authentication
- Admin login with **BCrypt-hashed** password
- Role-based access control (`ADMIN` / `CUSTOMER`)
- Loop until valid credentials are entered

### 📦 Product Management
- View all products
- Add / Edit / Delete products with confirmation
- Search by **brand** (fuzzy, case-insensitive)
- Filter by **price range**
- Search by **name + in-stock** only

### 👤 Customer Management
- View all customers
- Add / Edit / Delete customers
- Duplicate email detection (PostgreSQL `23505` constraint)

### 🧾 Invoice Management
- Create invoices with multiple products
- Auto stock deduction after purchase
- Stock availability check before confirming order
- View all invoices

### 🔍 Invoice Search
- Search by **customer name** (fuzzy)
- Search by **specific date**
- Search by **date range**

### 📊 Revenue Statistics
- Revenue grouped by **each day** of operation
- Revenue grouped by **each month** of operation
- Revenue grouped by **each year** of operation

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Database | PostgreSQL |
| DB Connectivity | JDBC (`postgresql-42.7.3.jar`) |
| Password Hashing | jBCrypt (`jbcrypt-0.4.jar`) |
| Build Tool | Maven |
| IDE | IntelliJ IDEA / VS Code |

---

## Project Structure

```
PhoneStoreManagement/
├── src/main/java/com/ra/
│   ├── config/
│   │   └── DBContext.java            # Database connection
│   ├── model/
│   │   ├── Customer.java             # Maps to CUSTOMER table
│   │   ├── Product.java              # Maps to PRODUCT table
│   │   ├── Invoice.java              # Maps to INVOICE table
│   │   └── InvoiceDetail.java        # Maps to INVOICE_DETAILS table
│   ├── repository/
│   │   ├── ICustomerRepository.java
│   │   ├── IProductRepository.java
│   │   ├── IInvoiceRepository.java
│   │   └── impl/
│   │       ├── CustomerRepository.java
│   │       ├── ProductRepository.java
│   │       └── InvoiceRepository.java
│   ├── service/
│   │   ├── ICustomerService.java
│   │   ├── IProductService.java
│   │   ├── IInvoiceService.java
│   │   └── impl/
│   │       ├── CustomerServiceImpl.java
│   │       ├── ProductServiceImpl.java
│   │       └── InvoiceServiceImpl.java
│   ├── presentation/
│   │   ├── LoginMenu.java
│   │   ├── MainMenu.java
│   │   ├── ProductMenu.java
│   │   ├── CustomerMenu.java
│   │   └── InvoiceMenu.java
│   ├── utils/
│   │   └── InputUtils.java           # Safe Scanner wrapper
│   └── Main.java                     # Entry point
├── src/main/resources/
│   └── db.properties                 # DB connection config
└── pom.xml
```

---

## Database Schema

```sql
-- 4 tables with FK relationships
CUSTOMER        (id, name, phone, email, password, role, address)
PRODUCT         (id, name, brand, price, stock)
INVOICE         (id, customer_id → CUSTOMER, created_at, total_amount)
INVOICE_DETAILS (id, invoice_id → INVOICE, product_id → PRODUCT,
                 quantity, unit_price)
```

> `unit_price` is stored at the time of purchase — independent of future product price changes.

---

## Getting Started

### Prerequisites

- Java 17+
- PostgreSQL 14+
- Maven 3.8+

### 1. Clone the repository

```bash
git clone https://github.com/anhtuanp-99/PhoneStoreManagement.git
cd PhoneStoreManagement
```

### 2. Create the database

```sql
CREATE DATABASE phone_store;
```

### 3. Run the schema

Connect to `phone_store` and execute:

```sql
CREATE TABLE customer (
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(100)  NOT NULL,
    phone    VARCHAR(30),
    email    VARCHAR(100)  UNIQUE NOT NULL,
    password VARCHAR(255)  NOT NULL,
    role     VARCHAR(20)   NOT NULL DEFAULT 'CUSTOMER',
    address  VARCHAR(255)
);

CREATE TABLE product (
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(100)  NOT NULL,
    brand VARCHAR(50)   NOT NULL,
    price DECIMAL(12,2) NOT NULL DEFAULT 0,
    stock INT           NOT NULL DEFAULT 0
);

CREATE TABLE invoice (
    id           SERIAL PRIMARY KEY,
    customer_id  INT           NOT NULL REFERENCES customer(id),
    created_at   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(12,2) NOT NULL DEFAULT 0
);

CREATE TABLE invoice_details (
    id         SERIAL PRIMARY KEY,
    invoice_id INT           NOT NULL REFERENCES invoice(id),
    product_id INT           NOT NULL REFERENCES product(id),
    quantity   INT           NOT NULL DEFAULT 1,
    unit_price DECIMAL(12,2) NOT NULL
);
```

### 4. Seed admin account

Password below is BCrypt hash of `admin123`:

```sql
INSERT INTO customer (name, phone, email, password, role)
VALUES (
    'Admin',
    '0900000000',
    'admin@store.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lNMy',
    'ADMIN'
);
```

> To generate your own hash, run `GenerateHash.java` with jBCrypt.

### 5. Configure database connection

Edit `src/main/resources/db.properties`:

```properties
db.url=jdbc:postgresql://localhost:5432/phone_store
db.username=postgres
db.password=YOUR_PASSWORD
```

### 6. Build and run

```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.ra.Main"
```

Or run `Main.java` directly from your IDE.

---

## Usage

```
╔══════════════════════════════════╗
║   HỆ THỐNG QUẢN LÝ CỬA HÀNG    ║
║         ĐIỆN THOẠI              ║
╠══════════════════════════════════╣
║  [1] Đăng nhập                  ║
║  [2] Đăng ký tài khoản          ║
║  [3] Thoát                      ║
╚══════════════════════════════════╝
```

Login with:
- **Email:** `admin@store.com`
- **Password:** `admin123`

---

## Architecture

The application follows a strict **4-layer architecture**:

```
┌─────────────────────────────────────────┐
│           Presentation Layer            │  ← Console menus, user input
├─────────────────────────────────────────┤
│             Service Layer               │  ← Business logic, validation,
│                                         │     BCrypt, Stream API
├─────────────────────────────────────────┤
│           Repository Layer              │  ← JDBC, SQL queries only
├─────────────────────────────────────────┤
│          PostgreSQL Database            │  ← Data persistence
└─────────────────────────────────────────┘
```

**Key design decisions:**

- **Dependency Injection** via constructor — Service receives Repository from outside, not `new` inside
- **Interface-based** — each layer depends on abstraction, not concrete implementation (DIP in SOLID)
- **PreparedStatement** everywhere — prevents SQL Injection
- **BCrypt** for password hashing — salt included, not reversible
- **Java Stream API** for all collection processing — no arrays used
- **try-with-resources** for all JDBC operations — no connection leaks
- **`RETURNING id`** after INSERT — safely retrieves auto-generated PK

---

## License

This project is for educational purposes.
