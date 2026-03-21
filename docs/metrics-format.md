# Monitoring Metrics Format

Workers collect monitoring results for each executed API check.
At this stage, the system uses a simplified metric structure aligned with the current domain model.

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

- latency (in milliseconds)
- HTTP status code
- timestamp of the request
- endpoint URL

The uptime will be calculated dynamically from monitoring results to avoid storing redundant aggregated metrics in the database.

##### Notes
- The success of a request can be derived from the HTTP status code (e.g., 2xx = success).
- Error information is not explicitly stored at this stage.
- Metrics are stored in-memory for the current prototype.

##### Aggregated Metrics (computed)

Aggregated metrics are calculated dynamically from raw request metrics and are not persisted.

These include:
- average latency
- uptime
- error rate
- percentiles (p95, p99)
- throughput
- status code distribution

The uptime is calculated dynamically based on successful vs total requests.