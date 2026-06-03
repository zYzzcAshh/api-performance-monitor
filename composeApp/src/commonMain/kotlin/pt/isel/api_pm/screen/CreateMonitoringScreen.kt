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
import pt.isel.api_pm.notification.NotificationConfig
import pt.isel.api_pm.theme.Primary
import pt.isel.api_pm.validation.CreateEndpointValidator
import pt.isel.api_pm.viewmodel.EndpointsViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
    var notificationType by remember { mutableStateOf("None") }
    var webhookUrl by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }

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

                    val notificationOptions =
                        listOf(
                            "None",
                            "Log",
                            "Discord",
                            "Slack",
                            "Email"
                        )

                    var expanded by remember {
                        mutableStateOf(false)
                    }

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = !expanded
                        }
                    ) {

                        OutlinedTextField(
                            value = notificationType,
                            onValueChange = {},
                            readOnly = true,
                            label = {
                                Text("Notification")
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {

                            notificationOptions.forEach { option ->

                                DropdownMenuItem(
                                    text = {
                                        Text(option)
                                    },
                                    onClick = {
                                        notificationType = option
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    when (notificationType) {

                        "Discord",
                        "Slack" -> {

                            AppTextField(
                                value = webhookUrl,
                                onValueChange = {
                                    webhookUrl = it
                                },
                                label = "Webhook URL",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        "Email" -> {

                            AppTextField(
                                value = email,
                                onValueChange = {
                                    email = it
                                },
                                label = "Email",
                                modifier = Modifier.fillMaxWidth()
                            )

                            AppTextField(
                                value = subject,
                                onValueChange = {
                                    subject = it
                                },
                                label = "Subject",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    val notification =
                        when (notificationType) {

                            "Discord" ->
                                NotificationConfig.DiscordWebhook(
                                    webhookUrl
                                )

                            "Slack" ->
                                NotificationConfig.SlackWebhook(
                                    webhookUrl
                                )

                            "Email" ->
                                NotificationConfig.Email(
                                    email,
                                    subject
                                )

                            "Log" ->
                                NotificationConfig.Log

                            else ->
                                NotificationConfig.None
                        }

                    val validation =
                        CreateEndpointValidator.validate(
                            name,
                            url,
                            interval,
                            notification
                        )

                    validation.error?.let {

                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    AppButton(
                        enabled =
                            validation.valid &&
                                    !state.creating,

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
                                interval,
                                notification
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