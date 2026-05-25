# System Architecture

The system is composed of the following main components:

- Client Application (Compose Multiplatform)
- Shared Multiplatform Module
- Backend Service (Ktor + Kotlin)
- Monitoring Workers
- Repository Layer
- PostgreSQL Database
- Local Monitoring Agent (future component)

---

# Overview

Users interact with the client application built using Compose Multiplatform.
The client communicates with the backend through a REST API implemented with Ktor.

The backend is responsible for:

- user authentication and authorization
- monitored endpoint management
- metric collection and aggregation
- monitoring orchestration
- notification handling
- alert processing

The system follows a layered architecture composed of:

- routes
- services
- repositories
- domain models
- DTOs

This separation improves maintainability, testability and modularity.

---

# Shared Multiplatform Module

The project uses Kotlin Multiplatform to share common code between backend and frontend.

Shared components include:

- DTOs
- serialization models
- validation logic
- common domain structures

This reduces duplication and improves consistency between platforms.

---

# Monitoring Workers

Monitoring workers execute periodic HTTP checks on registered endpoints.

These workers are responsible for:

- latency measurement
- status code collection
- uptime monitoring
- metric persistence

Collected metrics are stored through the repository layer and later aggregated into summaries and historical monitoring data.

[Monitoring Flow](monitoring-flow.png)

---

# Repository Layer

The persistence layer is abstracted through repository interfaces.

Currently, the system supports:

- in-memory repositories
- PostgreSQL repositories

This abstraction allows:

- easier testing
- modular persistence implementations
- gradual migration between storage solutions

---

# Database

The project is currently being migrated to PostgreSQL using Exposed ORM.

The database layer stores:

- users
- monitored endpoints
- monitoring metrics
- future alert-related data

---

# Authentication

Authentication is implemented using JWT tokens.

Protected routes require authenticated requests and validate user ownership for protected resources.

---

# Local Monitoring Agent (Future Work)

A local monitoring agent is planned for future implementation.

This component will allow monitoring of:

- private APIs
- internal infrastructure
- local network services

without directly exposing them to the public internet.

The architecture is being designed with security and centralized control in mind.

---

# Notes

The system is designed with modularity and multiplatform support as primary architectural goals.

Special attention has been given to:

- separation of concerns
- scalability
- testability
- reusable shared code
- clean REST API design

![System Architecture](system-architecture.png)