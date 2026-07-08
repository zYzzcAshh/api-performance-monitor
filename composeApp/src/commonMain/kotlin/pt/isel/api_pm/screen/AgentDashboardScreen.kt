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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import pt.isel.api_pm.components.ScreenContainer
import pt.isel.api_pm.components.charts.LatencyChart
import pt.isel.api_pm.components.charts.StatusCodeChart
import pt.isel.api_pm.components.dashboard.BigStatCard
import pt.isel.api_pm.components.dashboard.DashboardSectionCard
import pt.isel.api_pm.components.dashboard.InfoRow
import pt.isel.api_pm.components.dashboard.StatusChip
import pt.isel.api_pm.dto.metric.AgentAggregatedMetric
import pt.isel.api_pm.dto.metric.AgentRequestMetric
import pt.isel.api_pm.theme.Primary
import pt.isel.api_pm.utils.formatTimestamp
import pt.isel.api_pm.utils.roundTo
import pt.isel.api_pm.viewmodel.EndpointsViewModel
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun AgentDashboardScreen(
    agentId: UInt,
    viewModel: EndpointsViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(agentId) {
        while (true) {
            viewModel.loadAgentMetrics(agentId)
            viewModel.loadAgentSummary(agentId)
            delay(10000.milliseconds)
        }
    }

    val latestMetric = state.agentMetrics.lastOrNull()
    val summary = state.agentSummary
    val recentMetrics = state.agentMetrics.reversed().take(50)

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
                item { LatencyChart(metrics = state.agentMetrics) }
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
private fun CurrentStatusCard(metric: AgentRequestMetric) {
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

            InfoRow("Endpoint", metric.endpointName)
            InfoRow("Checked at", formatTimestamp(metric.timestamp.toString()))
        }
    }
}


// overview
@Composable
private fun OverviewCard(summary: AgentAggregatedMetric) {
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
private fun PerformanceCard(summary: AgentAggregatedMetric) {
    DashboardSectionCard(title = "Performance") {
        InfoRow("95th Percentile", "${summary.percentile95} ms")
        InfoRow("99th Percentile", "${summary.percentile99} ms")
        InfoRow("Throughput", "${summary.throughput} req/s")
    }
}


// monitoring window
@Composable
private fun MonitoringWindowCard(summary: AgentAggregatedMetric) {
    DashboardSectionCard(title = "Monitoring Window") {
        InfoRow("Started", formatTimestamp(summary.startTime.toString()))
        InfoRow("Last Metric", formatTimestamp(summary.endTime.toString()))
    }
}

// recent metric row card
@Composable
private fun MetricRowCard(metric: AgentRequestMetric) {
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