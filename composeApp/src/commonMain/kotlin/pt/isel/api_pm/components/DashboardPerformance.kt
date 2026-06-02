package pt.isel.api_pm.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pt.isel.api_pm.dto.metric.AggregatedMetric

@Composable
fun DashboardPerformance(
    summary: AggregatedMetric
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "Performance",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            MetricRow(
                label = "95th Percentile",
                value = "${summary.percentile95} ms"
            )

            MetricRow(
                label = "99th Percentile",
                value = "${summary.percentile99} ms"
            )

            MetricRow(
                label = "Throughput",
                value = "${summary.throughput} req/s"
            )
        }
    }
}