package pt.isel.api_pm.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pt.isel.api_pm.api.ApiClient

@Composable
fun EndpointsScreen(
    api: ApiClient,
    token: String
) {
    var endpoints by remember { mutableStateOf<String?>(null) }
    var monitoredEndpoints by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var creatingEndpoint by remember { mutableStateOf(false) }
    var creationMessage by remember { mutableStateOf<String?>(null) }

    var endpointName by remember { mutableStateOf("") }
    var endpointUrl by remember { mutableStateOf("") }
    var intervalSeconds by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            val result = api.getEndpointMetrics(token)
            if (result.isSuccess) {
                endpoints = result.getOrNull()
            } else {
                error = result.exceptionOrNull()?.message
            }
            isLoading = false
        }
    }

    fun getMonitoredEndpoints() {
        scope.launch {
            val result = api.getEndpoints(token)
            if (result.isSuccess) {
                monitoredEndpoints = result.getOrNull()
            }
            isLoading = false
        }
    }

    fun postRq() {
        scope.launch {
            if (endpointName.isBlank() || endpointUrl.isBlank() || intervalSeconds.isBlank()) {
                creationMessage = "Error: All fields are required"
                return@launch
            }
            
            val interval = intervalSeconds.toIntOrNull()
            if (interval == null || interval <= 0) {
                creationMessage = "Error: Interval must be a positive number"
                return@launch
            }

            creatingEndpoint = true
            creationMessage = null
            val result = api.createEndpointMonitor(token, endpointName, endpointUrl, interval)
            if (result.isSuccess) {
                creationMessage = "Endpoint created successfully: ${result.getOrNull()}"
                endpointName = ""
                endpointUrl = ""
                intervalSeconds = ""
                val refreshResult = api.getEndpointMetrics(token)
                if (refreshResult.isSuccess) {
                    endpoints = refreshResult.getOrNull()
                } else {
                    error = refreshResult.exceptionOrNull()?.message
                }
            } else {
                creationMessage = "Error creating endpoint: ${result.exceptionOrNull()?.message}"
            }
            creatingEndpoint = false
        }
    }

    fun getResults() {
        scope.launch {
            val result = api.getEndpointMetrics(token)
            if (result.isSuccess) {
                endpoints = result.getOrNull()
            } else {
                error = result.exceptionOrNull()?.message
            }
            isLoading = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = endpointName,
            onValueChange = { endpointName = it },
            label = { Text("Endpoint Name") },
            placeholder = { Text("e.g., My API") }
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = endpointUrl,
            onValueChange = { endpointUrl = it },
            label = { Text("Endpoint URL") },
            placeholder = { Text("e.g., https://api.example.com") }
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = intervalSeconds,
            onValueChange = { intervalSeconds = it },
            label = { Text("Interval (seconds)") },
            placeholder = { Text("e.g., 180") }
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = {postRq()}, enabled = !creatingEndpoint) {
            Text(text = "Create Endpoint Monitoring")
        }

        if (creatingEndpoint) {
            CircularProgressIndicator()
        }

        if (creationMessage != null) {
            Spacer(Modifier.height(8.dp))
            Text(creationMessage!!)
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = {getResults()}) {
            Text(text = "Refresh Endpoints Metrics for id 0")
        }

        when {
            isLoading -> CircularProgressIndicator()

            error != null -> Text("Error: $error")

            endpoints != null -> {
                Text("Endpoints:")
                Spacer(Modifier.height(8.dp))
                Text(endpoints!!)
            }
        }

        Button(onClick = {getMonitoredEndpoints()}) {
            Text(text = "Get Monitored Endpoints")
        }

        if (monitoredEndpoints != null) {
            Spacer(Modifier.height(8.dp))
            Text("Monitored Endpoints:")
            Text(monitoredEndpoints!!)
        }
    }
}