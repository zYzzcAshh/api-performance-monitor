package pt.isel.api_pm.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import pt.isel.api_pm.components.ScreenContainer
import pt.isel.api_pm.theme.Primary
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

    ScreenContainer {

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            TextButton(
                onClick = onBack
            ) {
                Text("← Back")
            }

            Text(
                text = "Endpoint Dashboard",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Primary
            )

            Card {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        text = "Metrics",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(12.dp))

                    state.endpoints?.let {

                        val items = it.removePrefix("[") // TODO parse metrics response properly
                            .removeSuffix("]")
                            .split("},")
                            .map { entry ->
                                if (!entry.trim().endsWith("}"))
                                    "$entry}"
                                else
                                    entry
                            }

                        items.forEach { metric ->
                            Text(metric)
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}