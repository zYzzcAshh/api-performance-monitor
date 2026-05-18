# API Performance Monitor

A modern observability platform for monitoring API performance, availability, and reliability, supporting both public and private services.

---

## Overview

**API Performance Monitor** is a distributed monitoring platform designed to help developers and teams monitor the health and availability of APIs in real time.

The system combines:
- A cloud-based backend service
- Monitoring workers
- Local monitoring agents
- A Compose Multiplatform client application

The platform periodically executes HTTP checks against registered endpoints and collects operational metrics such as:
- Response latency
- HTTP status codes
- Availability
- Request success/failure state

This enables users to detect failures early, monitor endpoint behaviour over time, and receive alerts when abnormal conditions occur.

---

## Features

- User authentication (register / login)
- Endpoint monitoring with configurable intervals
- Metrics collection and monitoring
- Alert rule configuration
- Discord webhook notifications
- Local monitoring agent for private/internal APIs
- Compose Multiplatform client application
- Asynchronous monitoring architecture

---

## Architecture

The system is composed of the following components:

- Client Application (Compose Multiplatform)
- Backend Service (Ktor)
- Monitoring Workers
- Local Monitoring Agent
- PostgreSQL Database

### Monitoring Flow

1. Users register endpoints to monitor
2. Workers execute periodic HTTP requests
3. Metrics are collected and evaluated
4. Results are stored by the backend
5. Alerts are triggered when configured conditions are met

Workers operate asynchronously and independently from the backend service.

---

## Metrics

Each monitoring execution generates a metric containing information such as:

```json
{
  "endpoint": "https://api.example.com",
  "timestamp": "2026-03-16T17:00:00Z",
  "latency": 123,
  "statusCode": 200
}
```

Collected data currently includes:
- Request latency
- HTTP status code
- Request timestamp
- Endpoint URL

Additional aggregation and analytics features are planned for future development.

---

## Data Model

### Main Entities

- Users
- Endpoints
- Monitoring Metrics
- Alert Rules
- Notifications

Each monitored endpoint generates time-series monitoring data over time.

---

## API

### Authentication
POST /auth/register
POST /auth/login

### Endpoints
GET /endpoints
POST /endpoints
DELETE /endpoints/{id}

### Metrics
GET /metrics/{endpoint}
GET /metrics/{endpointId}/summary
POST /metrics/check

### Users
GET /users
GET /users/{id}

---

## Example Request

```json
POST /endpoints
Content-Type: application/json

{
  "url": "https://api.example.com/users",
  "name": "Users API",
  "intervalSeconds": 60
}
```

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Kotlin + Ktor |
| Frontend | Compose Multiplatform |
| Database | PostgreSQL |
| Workers | Kotlin Coroutines |
| Agent | Kotlin |

---

## Project Status

Currently under development as part of the final-year project (PS 2025/2026).

### Current Progress

- ✅ Core backend architecture
- ✅ Authentication system
- ✅ Endpoint management
- ✅ Metrics collection
- ✅ Monitoring workflows
- ✅ Compose Multiplatform client
- 🚧 Database persistence improvements
- 🚧 Monitoring agents
- 🚧 Advanced dashboards & analytics

---

## Main Goals

- Provide a scalable API monitoring platform
- Support both public and private APIs
- Enable observability through metrics and alerts
- Explore distributed monitoring architectures
- Build a production-oriented monitoring system

---

## Team

- Francisco Aragão Dias
- Martim Ferreira

Supervisor:
- Pedro Pereira

---

## Future Work

- Advanced dashboards and visualizations
- Metrics aggregation and retention policies
- Distributed worker scaling
- Additional notification integrations
- TimescaleDB evaluation
- Enhanced monitoring agents

---

## License

© 2026 Francisco Aragão Dias | Martim Ferreira