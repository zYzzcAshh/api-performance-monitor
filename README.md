# API Performance Monitor

A modern observability platform for monitoring API performance, availability, and reliability with support for both public and private services.

---

## Overview

**API Performance Monitor** is a distributed monitoring platform designed to help developers and teams track the health of their APIs in real-time.

It combines:
- Cloud-based control plane (SaaS)
- Distributed monitoring workers
- Local agents for private / internal APIs

The platform continuously executes HTTP checks and collects key metrics such as:
- Latency
- Uptime
- Error rate
- Status codes

This allows users to detect failures early, analyze trends, and receive alerts when issues occur.

---

## Features

- User authentication (register / login)
- API endpoint monitoring (custom intervals)
- Real-time metrics collection (latency, status, success rate)
- Historical data & dashboards
- Configurable alerting system
- Local monitoring agent for private APIs
- Asynchronous monitoring workers

---

## Architecture

The system is composed of five main components:

- Client Application (Compose Multiplatform)
- Backend Service (Ktor + Kotlin)
- Monitoring Workers
- Local Monitoring Agent
- PostgreSQL Database

### Monitoring Flow

1. Users register endpoints to monitor  
2. Workers execute periodic HTTP requests  
3. Metrics are collected (latency, status, errors)  
4. Backend stores and evaluates results  
5. Alerts are triggered if needed  

Workers operate asynchronously and independently from the backend.

---

## Metrics

Each monitoring check generates a metric with the following structure:

```json
{
  "endpoint": "https://api.example.com",
  "timestamp": "2026-03-16T17:00:00Z",
  "latency": 123,
  "statusCode": 200
}
```

Collected metrics include:
- latency (in milliseconds)
- HTTP status code
- timestamp of the request
- endpoint URL

Uptime is computed dynamically based on monitoring results.

---

## Data Model

###### Main entities:
- Users
- Endpoints
- Monitoring Results

Each endpoint is periodically checked and produces time-series monitoring data. 

---

## API

### Auth
POST /auth/register  
POST /auth/login

### Endpoints
GET /endpoints
POST /endpoints
DELETE /endpoints/{id}

### Metrics
POST /metrics  
GET /endpoints/{id}/metrics

###### Example Request

```json
POST /endpoints
Content-Type: application/json

{
  "url": "https://api.example.com/users",
  "method": "GET",
  "interval": 60
}
```

---

## Tech Stack

| Layer       | Technology              |
|------------|------------------------|
| Backend     | Kotlin + Ktor          |
| Frontend    | Compose Multiplatform  |
| Database    | PostgreSQL             |
| Workers     | Kotlin (async jobs)    |
| Agent       | Kotlin (local runtime) |

---

## Project Status

Currently under development as part of the final-year project (PS 2025/2026).

### Current Progress
- ✅ Architecture defined
- ✅ Data model designed
- ✅ Monitoring flow specified
- 🚧 Backend implementation in progress
- ⏳ Frontend & dashboards

---

## Our Main Goals

- Provide a simple and scalable API monitoring solution
- Support both public and private APIs securely
- Enable observability through metrics and alerts
- Build a production-ready distributed system

---

## Team

- Francisco Aragão Dias  
- Martim Ferreira  

Supervisor:
- Pedro Pereira

---

## Future Work

- Alert integrations (Slack, Email, etc.)
- Advanced dashboards & visualizations
- Distributed scaling of workers
- Metrics aggregation & retention policies

---

## License

© 2026 Francisco Aragão Dias | Martim Ferreira. All rights reserved.
