package pt.isel.api_pm.components

import androidx.compose.runtime.Composable
import pt.isel.api_pm.dto.metric.RequestMetric
import pt.isel.api_pm.utils.formatTimestamp

@Composable
fun MetricDetails(
    metric: RequestMetric
) {

    MetricRow(
        label = "Status Code",
        value = metric.statusCode.toString()
    )

    MetricRow(
        label = "Latency",
        value = "${metric.latency} ms"
    )

    MetricRow(
        label = "Timestamp",
        value = formatTimestamp(metric.timestamp.toString())
    )

    MetricRow(
        label = "Endpoint",
        value = metric.endpoint.value
    )
}