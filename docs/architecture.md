# System Architecture

The system is composed of five main components:

- Web Frontend (React + TypeScript)
- Backend Service (Spring Boot + Kotlin)
- Monitoring Workers
- Local Monitoring Agent
- PostgreSQL Database

Users interact with the frontend which communicates with the backend REST API.
Monitoring workers periodically execute HTTP checks on registered endpoints and send monitoring metrics to the backend service.

The backend stores metrics and evaluates alert rules.

![System Architecture](./diagrams/system-architecture.png)