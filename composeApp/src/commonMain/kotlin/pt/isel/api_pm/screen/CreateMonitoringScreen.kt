package pt.isel.api_pm.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pt.isel.api_pm.components.AppButton
import pt.isel.api_pm.components.AppTextField
import pt.isel.api_pm.components.ScreenContainer
import pt.isel.api_pm.theme.Primary
import pt.isel.api_pm.viewmodel.EndpointsViewModel

@Composable
fun CreateMonitoringScreen(
    viewModel: EndpointsViewModel,
    onBack: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.message) {

        if (state.message == "Created successfully") {
            onBack()
        }
    }

    var name by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var interval by remember { mutableStateOf("") }

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
                text = "Create Monitoring",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Primary
            )

            Card {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    AppTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Endpoint Name",
                        modifier = Modifier.fillMaxWidth()
                    )

                    AppTextField(
                        value = url,
                        onValueChange = { url = it },
                        label = "URL",
                        modifier = Modifier.fillMaxWidth()
                    )

                    AppTextField(
                        value = interval,
                        onValueChange = { interval = it },
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
    }
}