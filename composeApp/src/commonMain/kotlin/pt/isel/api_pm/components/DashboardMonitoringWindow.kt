package pt.isel.api_pm.components

import androidx.compose.foundation.layout.Column
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
import pt.isel.api_pm.utils.formatTimestamp

@Composable
fun DashboardMonitoringWindow(
    summary: AggregatedMetric
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "Monitoring Window",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            MetricRow(
                label = "Started",
                value = formatTimestamp(
                    summary.startTime.toString()
                )
            )

            MetricRow(
                label = "Last Metric",
                value = formatTimestamp(
                    summary.endTime.toString()
                )
            )
        }
    }
}