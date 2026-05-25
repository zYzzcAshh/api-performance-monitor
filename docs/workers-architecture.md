# Monitoring Workers

Monitoring workers are responsible for executing periodic checks on registered API endpoints.

<p align="center">
  <img src="monitoring-flow.png" alt="Monitoring Flow" width="150">
</p>

The monitoring system operates asynchronously and independently from the main request-response flow of the backend API.

---

# Worker Responsibilities

Each monitoring worker is responsible for:

1. Retrieving monitored endpoints associated with a specific monitoring interval
2. Executing HTTP requests against target APIs
3. Measuring request latency
4. Collecting response metadata
5. Persisting monitoring metrics through the repository layer

Collected metrics include:

- response status code
- latency
- timestamp
- endpoint information

---

# Scheduling Model

The system currently uses interval-based workers.

Each worker is associated with a predefined monitoring interval, such as:

- 60 seconds
- 120 seconds
- 300 seconds
- 600 seconds

This architecture simplifies scheduling and reduces monitoring coordination complexity.

---

# Asynchronous Execution

Monitoring operations execute asynchronously using Kotlin coroutines.

This allows:

- non-blocking HTTP monitoring
- concurrent endpoint checks
- improved scalability
- reduced thread overhead

---

# Current Architecture

At the current stage, monitoring workers execute inside the backend service itself.

This simplifies deployment and development while allowing rapid iteration during the early project stages.

---

# Future Evolution

The architecture is being designed to support future distributed monitoring agents.

Future agents may:

- execute checks from different geographic locations
- monitor internal/private infrastructure
- operate independently from the central backend

This evolution will improve scalability, flexibility and observability coverage.