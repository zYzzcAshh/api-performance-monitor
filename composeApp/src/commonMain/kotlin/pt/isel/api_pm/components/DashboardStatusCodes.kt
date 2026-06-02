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
fun DashboardStatusCodes(
    summary: AggregatedMetric
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "Status Codes",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            summary.statusCodeDistribution
                .toList()
                .sortedBy { it.first }
                .forEach { pair ->

                    MetricRow(
                        label = "HTTP ${pair.first}",
                        value = pair.second.toString()
                    )
                }
        }
    }
}