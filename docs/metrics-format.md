# Monitoring Metrics Format

Workers collect monitoring results for each executed API check.

Example metric payload:

{
  "endpointId": "uuid",
  "timestamp": "2026-03-16T17:00:00Z",
  "latencyMs": 123,
  "statusCode": 200,
  "success": true,
  "error": null,
  "workerId": "worker-1"
}

Collected metrics include:

- latency
- HTTP status code
- success/failure
- error information
- timestamp

The uptime will be calculated dynamically from monitoring results to avoid storing redundant aggregated metrics in the database.