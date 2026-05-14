package pt.isel.api_pm.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pt.isel.api_pm.components.AppButton
import pt.isel.api_pm.components.AppTextField
import pt.isel.api_pm.components.EndpointCard
import pt.isel.api_pm.components.ScreenContainer
import pt.isel.api_pm.theme.Primary
import pt.isel.api_pm.theme.TextPrimary
import pt.isel.api_pm.theme.TextSecondary
import pt.isel.api_pm.viewmodel.EndpointsViewModel

@Composable
fun EndpointsScreen(
    viewModel: EndpointsViewModel,
    onLogout: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    var name by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var interval by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadMonitored()
    }

    ScreenContainer {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxSize()
        ) {

            item {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column {

                        Text(
                            text = "Dashboard",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "Monitor and manage your endpoints.",
                            color = TextSecondary
                        )
                    }

                    TextButton(
                        onClick = onLogout
                    ) {
                        Text("Logout")
                    }
                }
            }

            item {

                Card {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        Text(
                            text = "Create Endpoint",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        AppTextField(
                            value = name,
                            onValueChange = {
                                name = it
                            },
                            label = "Endpoint Name",
                            modifier = Modifier.fillMaxWidth()
                        )

                        AppTextField(
                            value = url,
                            onValueChange = {
                                url = it
                            },
                            label = "URL",
                            modifier = Modifier.fillMaxWidth()
                        )

                        AppTextField(
                            value = interval,
                            onValueChange = {
                                interval = it
                            },
                            label = "Interval (seconds)",
                            modifier = Modifier.fillMaxWidth()
                        )

                        AppButton(
                            text =
                                if (state.creating)
                                    "Creating..."
                                else
                                    "Create Endpoint",
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.createEndpoint(
                                    name,
                                    url,
                                    interval
                                )

                                name = ""
                                url = ""
                                interval = ""
                            }
                        )

                        state.message?.let {

                            Text(
                                text = it,
                                color =
                                    if (it.startsWith("Error"))
                                        MaterialTheme.colorScheme.error
                                    else
                                        Primary
                            )
                        }
                    }
                }
            }

            item {

                Text(
                    text = "Monitored Endpoints",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            if (state.isLoading) {

                item {
                    CircularProgressIndicator()
                }
            }

            if (state.monitoredEndpoints.isNotEmpty()) {

                items(state.monitoredEndpoints) { endpoint ->

                    EndpointCard(
                        title = endpoint.name,
                        url = endpoint.url,
                        interval = "${endpoint.intervalSeconds} seconds",

                        onViewMetrics = {
                            viewModel.loadMetrics(endpoint.id)
                        },

                        onDelete = {
                            viewModel.deleteEndpoint(endpoint.id)
                        }
                    )
                }
            }

            state.endpoints?.let {

                item {

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

                            // Temporary
                            val items = it.removePrefix("[")
                                .removeSuffix("]")
                                .split("},")
                                .map{ entry ->
                                    val fixed = if (!entry.trim().endsWith("}")) "$entry}" else entry
                                    fixed
                                }

                            items.forEach { text ->
                                Text(text)
                            }

                            //Text(it)
                        }
                    }
                }
            }

            state.error?.let {

                item {

                    Text(
                        text = "Error: $it",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}