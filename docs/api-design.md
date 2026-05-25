# API Design

## Authentication

### Register
`POST /auth/register`

Creates a new user account.

### Login
`POST /auth/login`

Authenticates a user and returns a JWT token.

---

# Users

### Get all users
`GET /users`

Returns all registered users.

### Get user by ID
`GET /users/{id}`

Returns information about a specific authenticated user.

Authentication required.

---

# Endpoints

### List monitored endpoints
`GET /endpoints`

Returns all monitored endpoints associated with the authenticated user.

Authentication required.

### Create monitored endpoint
`POST /endpoints`

Creates a new monitored endpoint.

Authentication required.

### Delete monitored endpoint
`DELETE /endpoints/{id}`

Deletes a monitored endpoint.

Authentication required.

---

# Metrics

### Get all metrics
`GET /metrics`

Returns all collected metrics.

Authentication required.

### Execute manual endpoint check
`POST /metrics/check`

Executes an immediate monitoring request for a given endpoint URL.

Authentication required.

### Get metrics by endpoint
`GET /metrics/{endpoint}`

Returns metrics associated with a monitored endpoint.

Authentication required.

### Get endpoint summary
`GET /metrics/{endpointId}/summary`

Returns aggregated monitoring statistics:
- uptime
- average latency
- total requests

Authentication required.

---

# Agent

### Register agent endpoint
`POST /agent/register`

Registers an endpoint through a monitoring agent.

Authentication required.

---

# Test Routes

### OK response
`GET /test/ok`

### Internal server error
`GET /test/error`

### Not found response
`GET /test/notfound`

### Slow response simulation
`GET /test/slow`

### Random response simulation
`GET /test/random`

### GitHub proxy request
`GET /test/github`