# CreateEndpointRequest

API request body for creating a new monitored endpoint. Defines where to ping, how often, how to notify on events, and under what conditions an alert fires.

---

## Top-level Fields

| Field | Type | Required | Description |
|---|---|---|---|
| `url` | `String` | ✅ | The HTTP(S) URL to monitor — this is the endpoint that gets periodically requested. |
| `name` | `String` | ✅ | Human-readable label for this endpoint, used in dashboards and notifications. |
| `intervalSeconds` | `Long` | ✅ | How frequently (in seconds) the endpoint is checked. E.g. `60` = once per minute. |
| `notification` | `NotificationConfig` | optional | How to deliver notifications. Defaults to `None` (no notifications). |
| `alertRule` | `AlertRule?` | optional | Rule that defines when an alert fires based on collected metrics. Defaults to `null` (no alerting). |

---

## `NotificationConfig`

Sealed class controlling how the system notifies you. Serialized as a discriminated union using a `type` field.

| Variant (`type`) | Behaviour |
|---|---|
| `"none"` → `None` | No notification sent. This is the default when the field is omitted. |
| `"log"` → `Log` | Event is written to the server log only. Useful during development. |
| `"discord_webhook"` → `DiscordWebhook` | Sends a message to a Discord channel via a webhook URL. |
| `"email"` → `Email` | Sends an email to the specified address with a custom subject. |

### `DiscordWebhook` fields

| Field | Type | Description |
|---|---|---|
| `webhookUrl` | `String` | Full Discord incoming webhook URL (from your server's channel integrations). |

### `Email` fields

| Field | Type | Description |
|---|---|---|
| `to` | `String` | Recipient email address. |
| `subject` | `String` | Subject line of the notification email. |

---

## `AlertRule`

Sealed class describing the condition under which an alert fires. Every rule operates on a sliding window of `RequestMetric` samples and applies an aggregation strategy before evaluating the condition.

### Common fields (all variants)

| Field | Type | Description |
|---|---|---|
| `durationSeconds` | `Long` | Time window (in seconds) of metrics to consider, counted backwards from the moment of the request. Only metrics collected within the last `durationSeconds` seconds are used. |
| `aggregation` | `AggregationType` | How to reduce the window of values before applying the condition (see `AggregationType`). |

### `StatusCodeRule`

Fires when HTTP status codes returned by the endpoint satisfy a given condition.

| Field | Type | Description |
|---|---|---|
| `operator` | `ComparisonOperator` | How to compare the (aggregated) status code to `value`. |
| `value` | `Int` | Status code to compare against (e.g. `500`). |

### `LatencyRule`

Fires when request latency satisfies a given condition.

| Field | Type | Description |
|---|---|---|
| `operator` | `ComparisonOperator` | How to compare the (aggregated) latency to `value`. |
| `value` | `Long` | Latency threshold in milliseconds. |

### `DownTimeRule`

Fires when the endpoint is considered down (status code ≥ 500). Does not use a `ComparisonOperator` — it counts failures directly.

| Field | Type | Description |
|---|---|---|
| `aggregation` | `AggregationType` | Defaults to `ALL` — every request in the window must be a failure for the alert to fire. |

---

## `AggregationType`

Controls how a window of metric values is reduced before being evaluated against the rule condition.

| Variant | Behaviour |
|---|---|
| `ALL` | The condition must hold for **every** metric in the window. All-or-nothing. |
| `AVG` | Values are averaged; the condition is applied to that average. Smooths out spikes. |
| `COUNT(count: Int)` | The condition is evaluated per-metric; the alert fires only if at least `count` metrics satisfy it. Good for "N failures in a row" style rules. |

---

## `ComparisonOperator`

Used by `StatusCodeRule` and `LatencyRule` to compare the aggregated value to the threshold.

| Value | Meaning |
|---|---|
| `GT` | `>` |
| `GTE` | `≥` |
| `LT` | `<` |
| `LTE` | `≤` |
| `EQ` | `==` |

---

## `AlertEvaluator`

Runtime engine that decides whether to fire an alert given a list of collected metrics and an active `AlertRule`. Returns `false` immediately if the metrics list is empty.

### Evaluation logic

| Rule | Aggregation | Trigger condition |
|---|---|---|
| `LatencyRule` | `ALL` | Every latency in the window satisfies `operator(latency, value)`. |
| `LatencyRule` | `AVG` | The average latency satisfies `operator(avg, value)`. |
| `LatencyRule` | `COUNT(n)` | At least `n` latencies satisfy the condition. |
| `StatusCodeRule` | `ALL` | Every status code satisfies `operator(code, value)`. |
| `StatusCodeRule` | `AVG` | The average status code satisfies `operator(avg, value)`. |
| `StatusCodeRule` | `COUNT(n)` | At least `n` status codes satisfy the condition. |
| `DownTimeRule` | `ALL` | All requests in the window returned status ≥ 500. |
| `DownTimeRule` | `AVG` | ≥ 50% of requests in the window returned status ≥ 500. |
| `DownTimeRule` | `COUNT(n)` | At least `n` requests returned status ≥ 500. |