# Scaler Ecommerce Project (Backend Specialization Capstone)

This is a microservices-based ecommerce backend built as part of the Scaler Neovarsity Capstone Project. It uses Spring Boot 3.2.x and Spring Cloud 2023.0.x.

## Architecture & Technology Stack
- **Architecture**: Microservices (Spring Cloud, API Gateway, Eureka Discovery Server)
- **Databases & Infrastructure**: MySQL / PostgreSQL, Redis, Apache Kafka (Amazon MSK)
- **Authentication**: JWT & OAuth2 (Google)
- **External Integrations**: Stripe (Payments), Gemini AI (Semantic Search)

## Features & Epics
1. **Epic 1: Identity & Security Foundation** - JWT Registration/Login and OAuth2 (API Gateway, UserAuthService).
2. **Epic 2: Product Discovery & AI Semantic Search** - Paginated, cached product listing with Redis and AI Semantic Search using Gemini (CatalogService).
3. **Epic 3: Cart Management & Stripe Checkout** - Cart additions, Order creation, and Stripe checkout initiation & webhook reconciliation (OrderService, PaymentService).
4. **Epic 4: Asynchronous Fulfillment & AI Notifications** - Kafka event producers and consumers for payment and order processing.
5. **Epic 5: Basic React UI Frontend** - A simple UI for validation.

## Local Setup
Ensure you have Docker, Java 17+, and Maven installed.
1. Run `docker-compose up -d` to start dependencies (Redis, Kafka, DBs).
2. Start Eureka Discovery Server (`discovery-server`).
3. Start API Gateway (`api-gateway`).
4. Start the individual microservices (`user-auth-service`, `catalog-service`, etc.).

## Iterative Development (BMad Workflow)
This project enforces a strict BMad iterative workflow for AI agents:
1. `/bmad-create-story`
2. `/bmad-dev-story`
3. `/bmad-code-review`
Testing and compilation verification (`mvn clean test`) are mandatory for every story.
