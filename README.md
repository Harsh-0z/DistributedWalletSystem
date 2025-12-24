# 🏦 Distributed Wallet System (Saga Pattern)

A high-performance, fault-tolerant distributed wallet system built with **Spring Boot Microservices**.

This project demonstrates the **Saga Orchestration Pattern** to handle distributed transactions across heterogeneous databases (**Oracle** & **MySQL**), ensuring data consistency without using 2PC (Two-Phase Commit).

## 🚀 Key Features

* **Microservices Architecture:** Independently deployable services for Scalability.
* **Saga Pattern (Orchestration):** Manages distributed transactions (Debit A → Credit B) with automatic **Compensation** (Rollback/Refund) on failure.
* **Polyglot Persistence:**
    * **Service A:** Uses **Oracle Database**.
    * **Service B:** Uses **MySQL Database**.
* **Concurrency Control:** Implements **Pessimistic Locking** (`SELECT ... FOR UPDATE`) to prevent race conditions and double-spending.
* **Self-Healing Mechanism:** A background **Recovery Scheduler** automatically detects and fixes "stuck" transactions (Zombies) caused by server crashes.
* **Service Discovery:** Integrated with **Netflix Eureka** for dynamic service registration.

## 🛠️ Tech Stack

* **Java 17**
* **Spring Boot 3.x**
* **Spring Data JPA (Hibernate)**
* **Spring Cloud Netflix Eureka**
* **Spring Cloud OpenFeign**
* **Databases:** Oracle 19c/21c, MySQL 8
* **Build Tool:** Maven/Gradle

## 🏗️ System Architecture

The system consists of 4 main components:

1.  **Eureka Server (8761):** Service Registry.
2.  **Wallet Service A (8081):** Manages users on Oracle DB.
3.  **Wallet Service B (8082):** Manages users on MySQL DB.
4.  **Orchestrator Service (8080):** The "Brain" that coordinates the transaction lifecycle.

### Transaction Flow (Happy Path)
1.  User requests transfer via Orchestrator.
2.  Orchestrator creates a `STARTED` event in its Journal.
3.  **Phase 1:** Call Service A to **Debit** sender.
4.  **Phase 2:** Call Service B to **Credit** receiver.
5.  Orchestrator marks transaction as `COMPLETED`.

### Compensation Flow (Failure)
1.  **Phase 1** succeeds (Debit).
2.  **Phase 2** fails (Receiver not found / Network error).
3.  Orchestrator catches exception.
4.  **Compensation:** Calls Service A to **Refund** the money.
5.  Transaction marked as `REFUNDED`.

## ⚙️ Setup & Installation

### 1. Prerequisites
* Java 17+ installed.
* Oracle Database & MySQL Database running.
* Maven or Gradle.

### 2. Configure Databases
Update the `application.properties` in `wallet-service-a` and `wallet-service-b` with your DB credentials.

### 3. Run the Services
Start the applications in the following order:
1.  **Eureka Server** (Port 8761)
2.  **Wallet Service A** (Port 8081)
3.  **Wallet Service B** (Port 8082)
4.  **Orchestrator Service** (Port 8080)

## 🔌 API Endpoints

### 1. Create User (Service A - Oracle)
**POST** `http://localhost:8081/api/wallet/create`
```json
{
  "username": "Alice",
  "password": "password123",
  "email": "alice@example.com",
  "initialBalance": 1000.00
}
2. Create User (Service B - MySQL)
POST http://localhost:8082/api/wallet/create

JSON

{
  "username": "Bob",
  "password": "securepass",
  "email": "bob@example.com",
  "initialBalance": 0.00
}


3. Perform Transfer (Orchestrator)
POST http://localhost:8080/api/orchestrator/transfer

JSON

{
  "senderUsername": "Alice",
  "senderService": "A",
  "receiverUsername": "Bob",
  "receiverService": "B",
  "amount": 100.00
}


🛡️ Resilience & Recovery
The system handles Infrastructure Failures (e.g., Orchestrator crashes after Debit but before Credit).

Stateful Journal: Every step is saved to the saga_transaction table.

Recovery Scheduler: A scheduled task runs every minute to find transactions stuck in DEBIT_COMPLETED state and auto-refunds the user.



👨‍💻 Author
Developed by Harsh Patel as a demonstration of Advanced Distributed Systems with Spring Boot.