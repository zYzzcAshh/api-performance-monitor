package pt.isel.api_pm.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pt.isel.api_pm.dto.metric.AggregatedMetric
import pt.isel.api_pm.utils.roundTo

@Composable
fun DashboardOverview(
    summary: AggregatedMetric
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "Overview",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                DashboardMetricCard(
                    title = "Avg Latency",
                    value = "${summary.averageLatency.roundTo(1)} ms"
                )

                DashboardMetricCard(
                    title = "Uptime",
                    value = "${summary.uptime}%"
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                DashboardMetricCard(
                    title = "Requests",
                    value = summary.totalRequests.toString()
                )

                DashboardMetricCard(
                    title = "Error Rate",
                    value = "${summary.errorRate.roundTo(2)}%"
                )
            }
        }
    }
}