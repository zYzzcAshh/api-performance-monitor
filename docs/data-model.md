# Data Model

The system currently uses PostgreSQL as the primary persistence layer.

The database model is organized around three main entities:

- users
- endpoints
- request metrics

Additional fields were introduced to support:
- notifications
- alert rules
- monitoring configuration

---

# users

Stores registered platform users.

| field | type |
|------|------|
| id | integer |
| username | varchar |
| password_hash | varchar |
| created_at | timestamp |

### Notes

- `id` is auto-generated
- passwords are stored as hashed values only
- usernames are unique identifiers for authentication

---

# endpoints

Stores monitored API endpoints associated with users.

| field | type |
|------|------|
| id | integer |
| user_id | integer (FK) |
| url | varchar |
| name | varchar |
| interval_seconds | long |
| created_at | timestamp |
| notification_type | varchar |
| notification_data | text nullable |
| alert_rule_type | varchar nullable |
| alert_rule_data | text nullable |

### Notes

- each endpoint belongs to a user
- monitoring intervals are predefined and validated
- notification settings are persisted dynamically
- alert rules are serialized and stored for future evaluation

---

# request_metrics

Stores raw monitoring request metrics.

| field | type |
|------|------|
| id | long |
| user_id | integer (FK) |
| endpoint_id | integer (FK) |
| url | varchar |
| timestamp | timestamp |
| latency | long |
| status_code | integer |

### Notes

- metrics are stored as raw monitoring data
- aggregated statistics are calculated dynamically
- request success is derived from HTTP status codes
- metrics are associated both with users and monitored endpoints

---

# Relationships

## users → endpoints

One user can own multiple monitored endpoints.

## endpoints → request_metrics

One monitored endpoint can generate multiple request metrics over time.

---

# Architectural Notes

The persistence layer was designed to remain storage-independent through repository abstraction.

The system currently supports:

- in-memory repositories
- PostgreSQL repositories

This allows:
- easier testing
- gradual migration
- interchangeable persistence implementations