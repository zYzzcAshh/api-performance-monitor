package pt.isel.api_pm.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import pt.isel.api_pm.components.ScreenContainer
import pt.isel.api_pm.components.charts.LatencyChart
import pt.isel.api_pm.components.charts.StatusCodeChart
import pt.isel.api_pm.dto.metric.AggregatedMetric
import pt.isel.api_pm.dto.metric.RequestMetric
import pt.isel.api_pm.theme.Primary
import pt.isel.api_pm.utils.formatTimestamp
import pt.isel.api_pm.utils.roundTo
import pt.isel.api_pm.viewmodel.EndpointsViewModel
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun EndpointDashboardScreen(
    endpointId: UInt,
    viewModel: EndpointsViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(endpointId) {
        while (true) {
            viewModel.loadMetrics(endpointId)
            viewModel.loadSummary(endpointId)
            delay(30000.milliseconds)
        }
    }

    val latestMetric = state.metrics.lastOrNull()
    val summary = state.summary
    val recentMetrics = state.metrics.reversed().take(50)

    ScreenContainer {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {

            // top bar
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 8.dp)
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Primary
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Dashboard",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                }
            }

            // current status
            latestMetric?.let { metric ->
                item {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + expandVertically()
                    ) {
                        CurrentStatusCard(metric)
                    }
                }
            }

            // summary cards
            summary?.let { s ->
                item { OverviewCard(s) }
                item { PerformanceCard(s) }
                item { LatencyChart(metrics = state.metrics) }
                item { MonitoringWindowCard(s) }
                item {
                    StatusCodeChart(
                        distribution = s.statusCodeDistribution
                    )
                }
            }

            // recent metrics header
            item {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Recent Metrics",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // metric rows
            items(recentMetrics) { metric ->
                MetricRowCard(metric)
            }
        }
    }
}


// current status
@Composable
private fun CurrentStatusCard(metric: RequestMetric) {
    val isOk = metric.statusCode in 200..299
    val statusColor = if (isOk) Color(0xFF22C55E) else Color(0xFFEF4444)

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Current Status",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                // status pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(statusColor.copy(alpha = 0.15f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isOk) "● Online" else "● Degraded",
                        color = statusColor,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 0.5.dp
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                StatusChip(
                    label = "Status",
                    value = metric.statusCode.toString(),
                    color = statusColor,
                    modifier = Modifier.weight(1f)
                )
                StatusChip(
                    label = "Latency",
                    value = "${metric.latency} ms",
                    color = Primary,
                    modifier = Modifier.weight(1f)
                )
            }

            InfoRow("Endpoint", metric.endpoint.value)
            InfoRow("Checked at", formatTimestamp(metric.timestamp.toString()))
        }
    }
}


// overview
@Composable
private fun OverviewCard(summary: AggregatedMetric) {
    DashboardSectionCard(title = "Overview") {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            BigStatCard(
                label = "Avg Latency",
                value = "${summary.averageLatency.roundTo(1)}",
                unit = "ms",
                modifier = Modifier.weight(1f)
            )
            BigStatCard(
                label = "Uptime",
                value = "${summary.uptime}",
                unit = "%",
                color = Color(0xFF22C55E),
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            BigStatCard(
                label = "Requests",
                value = summary.totalRequests.toString(),
                unit = "total",
                modifier = Modifier.weight(1f)
            )
            BigStatCard(
                label = "Error Rate",
                value = "${summary.errorRate.roundTo(2)}",
                unit = "%",
                color = if (summary.errorRate > 5.0) Color(0xFFEF4444) else Color(0xFF22C55E),
                modifier = Modifier.weight(1f)
            )
        }
    }
}


// performance
@Composable
private fun PerformanceCard(summary: AggregatedMetric) {
    DashboardSectionCard(title = "Performance") {
        InfoRow("95th Percentile", "${summary.percentile95} ms")
        InfoRow("99th Percentile", "${summary.percentile99} ms")
        InfoRow("Throughput", "${summary.throughput} req/s")
    }
}


// monitoring window
@Composable
private fun MonitoringWindowCard(summary: AggregatedMetric) {
    DashboardSectionCard(title = "Monitoring Window") {
        InfoRow("Started", formatTimestamp(summary.startTime.toString()))
        InfoRow("Last Metric", formatTimestamp(summary.endTime.toString()))
    }
}


// status Codes
@Composable
private fun StatusCodesCard(summary: AggregatedMetric) {
    DashboardSectionCard(title = "Status Codes") {
        summary.statusCodeDistribution
            .toList()
            .sortedBy { it.first }
            .forEach { (code, count) ->
                val color = when (code) {
                    in 200..299 -> Color(0xFF22C55E)
                    in 300..399 -> Color(0xFFF59E0B)
                    in 400..499 -> Color(0xFFEF4444)
                    in 500..599 -> Color(0xFFDC2626)
                    else        -> MaterialTheme.colorScheme.onSurfaceVariant
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(color.copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "HTTP $code",
                            color = color,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = count.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
    }
}


// recent metric row card
@Composable
private fun MetricRowCard(metric: RequestMetric) {
    val isOk = metric.statusCode in 200..299
    val statusColor = if (isOk) Color(0xFF22C55E) else Color(0xFFEF4444)

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // status code badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(statusColor.copy(alpha = 0.12f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = metric.statusCode.toString(),
                    color = statusColor,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            // latency
            Text(
                text = "${metric.latency} ms",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Primary
            )

            // timestamp
            Text(
                text = formatTimestamp(metric.timestamp.toString()),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


// reusable primitives
@Composable
private fun DashboardSectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 0.5.dp
            )
            content()
        }
    }
}

@Composable
private fun BigStatCard(
    label: String,
    value: String,
    unit: String,
    color: Color = Primary,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text(
                    text = unit,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 3.dp)
                )
            }
        }
    }
}

@Composable
private fun StatusChip(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = color.copy(alpha = 0.8f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}