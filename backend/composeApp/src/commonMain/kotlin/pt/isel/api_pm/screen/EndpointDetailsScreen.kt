package pt.isel.api_pm.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pt.isel.api_pm.components.ScreenContainer
import pt.isel.api_pm.theme.Primary

@Composable
fun EndpointDetailsScreen() {

    ScreenContainer {

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Endpoint Metrics",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Primary
            )

            Card {

                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text("Latency: -- ms")

                    Text("Status: --")

                    Text("Uptime: --")

                    Text("Requests: --")
                }
            }
        }
    }
}