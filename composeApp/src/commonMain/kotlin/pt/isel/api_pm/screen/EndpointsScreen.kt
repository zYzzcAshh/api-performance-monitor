package pt.isel.api_pm.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
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
import pt.isel.api_pm.components.AppButton
import pt.isel.api_pm.components.EndpointCard
import pt.isel.api_pm.components.ScreenContainer
import pt.isel.api_pm.theme.Primary
import pt.isel.api_pm.theme.TextPrimary
import pt.isel.api_pm.theme.TextSecondary
import pt.isel.api_pm.viewmodel.EndpointsViewModel

@Composable
fun EndpointsScreen(
    viewModel: EndpointsViewModel,
    onLogout: () -> Unit,
    onCreateMonitoring: () -> Unit,
    onOpenEndpoint: (UInt) -> Unit

) {

    val state by viewModel.state.collectAsState()

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

                AppButton(
                    text = "Create Monitoring",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCreateMonitoring
                )
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
                            onOpenEndpoint(endpoint.id)
                        },

                        onDelete = {
                            viewModel.deleteEndpoint(endpoint.id)
                        }
                    )
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