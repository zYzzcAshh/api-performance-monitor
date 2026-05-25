# Monitoring Metrics Format

Monitoring workers collect request metrics for each executed API check.

The system currently stores raw monitoring metrics associated with monitored endpoints.

Example metric payload:

```json
{
  "endpoint": "https://api.example.com",
  "timestamp": "2026-03-16T17:00:00Z",
  "latency": 123,
  "statusCode": 200
}
```

Collected metrics include:

- endpoint URL
- request timestamp
- latency (milliseconds)
- HTTP status code

The monitoring model intentionally stores raw request data rather than precomputed aggregates.

This allows aggregated statistics to be dynamically recalculated whenever necessary.

---

# Aggregated Metrics

Aggregated metrics are computed dynamically from raw monitoring data and are not directly persisted.

Current aggregated metrics include:

- uptime
- average latency
- total requests

Additional planned metrics include:

- error rate
- latency percentiles (p95, p99)
- throughput
- status code distribution

---

# Uptime Calculation

Uptime is calculated dynamically based on successful versus total requests.

Successful requests are currently defined as responses with HTTP status codes in the `2xx` range.

Example:

```text
uptime = successful_requests / total_requests * 100
```

This avoids storing redundant aggregated state and ensures summaries remain consistent with the underlying monitoring history.

---

# Notes

- Request success is derived from the HTTP status code.
- Monitoring metrics are currently supported by both in-memory and PostgreSQL repository implementations.
- The monitoring structure was designed to remain storage-independent through repository abstraction.
- Future versions may include richer error metadata and distributed monitoring support.