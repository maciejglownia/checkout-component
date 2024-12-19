# checkout-component

### Architecture Design

The service will use:
- Service-Oriented Architecture (SOA) with an API-first approach.
- Spring Boot for application boilerplate.
- Jakarta EE for any additional enterprise-specific features.
- Stateless REST APIs except for the calculation logic (stateful checkout mechanism).
Layered architecture:
- Controller Layer: Exposes REST APIs.
- Service Layer: Implements business logic.
- Repository Layer: Handles data persistence (if applicable; or we can use an in-memory model for simplicity).

