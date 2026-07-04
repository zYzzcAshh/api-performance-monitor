package pt.isel.api_pm.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.api_pm.alert.AggregationType
import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.alert.ComparisonOperator
import pt.isel.api_pm.components.AppButton
import pt.isel.api_pm.components.AppTextField
import pt.isel.api_pm.components.ScreenContainer
import pt.isel.api_pm.domain.endpoint.DurationSeconds
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
    var alertType by remember { mutableStateOf("Status Code") }
    var alertOperator by remember { mutableStateOf("Greater Than (>)") }
    var alertValue by remember { mutableStateOf("500") }
    var alertDuration by remember { mutableStateOf("60") }
    var aggregation by remember { mutableStateOf("Occurrences") }
    var aggregationCount by remember { mutableStateOf("1") }

    LaunchedEffect(alertType) {

        if (alertType == "Down Time") {

            alertValue = ""
            aggregation = "Occurrences"
            aggregationCount = "1"
        }
    }

    val operator = when (alertOperator) {
        "Greater Than (>)"          -> ComparisonOperator.GT
        "Greater Than or Equal (>=)"-> ComparisonOperator.GTE
        "Less Than (<)"             -> ComparisonOperator.LT
        "Less Than or Equal (<=)"   -> ComparisonOperator.LTE
        else                        -> ComparisonOperator.EQ
    }

    val aggregationType =
        when (aggregation) {

            "Any request" ->
                AggregationType.ALL

            "Average" ->
                AggregationType.AVG

            else ->
                AggregationType.COUNT(
                    aggregationCount.toIntOrNull() ?: 1
                )
        }

    val alertRule = when (alertType) {
        "Status Code" -> AlertRule.StatusCodeRule(
            operator = operator,
            value = alertValue.toIntOrNull() ?: 500,
            durationSeconds = DurationSeconds(alertDuration.toLongOrNull() ?: 60),
            aggregation = aggregationType
        )
        "Latency" -> AlertRule.LatencyRule(
            operator = operator,
            value = alertValue.toLongOrNull() ?: 1000,
            durationSeconds = DurationSeconds(alertDuration.toLongOrNull() ?: 60),
            aggregation = aggregationType
        )
        else -> AlertRule.DownTimeRule(
            durationSeconds = DurationSeconds(alertDuration.toLongOrNull() ?: 60)
        )
    }

    val notification = when (notificationType) {
        "Discord" -> NotificationConfig.DiscordWebhook(webhookUrl)
        "Slack"   -> NotificationConfig.SlackWebhook(webhookUrl)
        "Email"   -> NotificationConfig.Email(email, subject)
        "Log"     -> NotificationConfig.Log
        else      -> NotificationConfig.None
    }

    val validation = CreateEndpointValidator.validate(name, url, interval, notification)

    ScreenContainer {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // top bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Primary
                    )
                }
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "New Monitor",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            }

            Spacer(Modifier.height(8.dp))

            // section: endpoint
            SectionCard(
                title = "Endpoint",
                subtitle = "What do you want to monitor?"
            ) {
                AppTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Name",
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
                    label = "Check interval (seconds)",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(12.dp))

            // section: notifications
            SectionCard(
                title = "Notifications",
                subtitle = "How should we alert you?",
                icon = Icons.Default.Notifications
            ) {
                StyledDropdown(
                    label = "Channel",
                    selected = notificationType,
                    options = listOf("None", "Log", "Discord", "Slack", "Email"),
                    onSelected = { notificationType = it }
                )

                AnimatedVisibility(
                    visible = notificationType == "Discord" || notificationType == "Slack",
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    AppTextField(
                        value = webhookUrl,
                        onValueChange = { webhookUrl = it },
                        label = "Webhook URL",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }

                AnimatedVisibility(
                    visible = notificationType == "Email",
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        AppTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Email address",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )
                        AppTextField(
                            value = subject,
                            onValueChange = { subject = it },
                            label = "Subject",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // section: alert rule
            SectionCard(
                title = "Alert Rule",
                subtitle = "When should an alert be triggered?",
                icon = Icons.Default.Warning
            ) {

                StyledDropdown(
                    label = "Trigger type",
                    selected = alertType,
                    options = listOf(
                        "Status Code",
                        "Latency",
                        "Down Time"
                    ),
                    onSelected = {
                        alertType = it
                    }
                )

                AnimatedVisibility(
                    visible = alertType != "Down Time",
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        StyledDropdown(
                            label = "Aggregation",
                            selected = aggregation,
                            options = listOf(
                                "Any request",
                                "Average",
                                "Occurrences"
                            ),
                            onSelected = {
                                aggregation = it
                            }
                        )

                        AnimatedVisibility(
                            visible = aggregation == "Occurrences",
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {

                            AppTextField(
                                value = aggregationCount,
                                onValueChange = {
                                    aggregationCount = it
                                },
                                label = "Occurrences",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            )
                        }

                        StyledDropdown(
                            label = "Condition",
                            selected = alertOperator,
                            options = listOf(
                                "Greater Than (>)",
                                "Greater Than or Equal (>=)",
                                "Less Than (<)",
                                "Less Than or Equal (<=)",
                                "Equal (=)"
                            ),
                            onSelected = {
                                alertOperator = it
                            }
                        )

                        AppTextField(
                            value = alertValue,
                            onValueChange = {
                                alertValue = it
                            },
                            label =
                                if (alertType == "Status Code")
                                    "Status code"
                                else
                                    "Latency (ms)",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                AppTextField(
                    value = alertDuration,
                    onValueChange = { alertDuration = it },
                    label = "Duration (seconds)",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = if (alertType == "Down Time") 0.dp else 8.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            // validation error
            AnimatedVisibility(
                visible = validation.error != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                validation.error?.let { error ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.errorContainer)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }

            // submit
            AppButton(
                enabled = validation.valid && !state.creating,
                text = if (state.creating) "Creating…" else "Create Monitor",
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.createEndpoint(name, url, interval, notification, alertRule)
                }
            )

            // api message
            state.message?.let { msg ->
                Spacer(Modifier.height(12.dp))
                Text(
                    text = msg,
                    color = if (msg.startsWith("Error"))
                        MaterialTheme.colorScheme.error
                    else
                        Primary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}


// reusable section card
@Composable
private fun SectionCard(
    title: String,
    subtitle: String,
    icon: ImageVector? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 0.5.dp
            )

            content()
        }
    }
}


// styled dropdown (replaces the raw ExposedDropdownMenuBox boilerplate)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StyledDropdown(
    label: String,
    selected: String,
    options: List<String>,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}