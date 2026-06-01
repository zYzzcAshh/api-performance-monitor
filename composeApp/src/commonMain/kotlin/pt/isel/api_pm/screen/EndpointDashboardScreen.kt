package pt.isel.api_pm.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import pt.isel.api_pm.components.MetricDetails
import pt.isel.api_pm.components.ScreenContainer
import pt.isel.api_pm.theme.Primary
import pt.isel.api_pm.theme.TextPrimary
import pt.isel.api_pm.components.MetricRow
import pt.isel.api_pm.utils.formatTimestamp
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
        while(true) {
            viewModel.loadMetrics(endpointId)
            delay(30000.milliseconds) // auto refresh (30sec is low or high?) >> perguntar
        }
    }

    val latestMetric =
        state.metrics.lastOrNull()

    ScreenContainer {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                TextButton(
                    onClick = onBack
                ) {
                    Text("← Back")
                }
            }

            item {
                Text(
                    text = "Endpoint Dashboard",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            }

            latestMetric?.let { metric ->

                item {

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {

                            Text(
                                text = "Current Status",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(12.dp))

                            MetricDetails(metric)
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Metrics",
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            items(state.metrics.reversed()) { metric ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        MetricDetails(metric)
                    }
                }
            }
        }
    }
}