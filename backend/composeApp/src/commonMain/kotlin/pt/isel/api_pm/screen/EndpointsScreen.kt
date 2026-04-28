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
import androidx.compose.runtime.collectAsState
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
import pt.isel.api_pm.viewmodel.EndpointsViewModel

@Composable
fun EndpointsScreen(viewModel: EndpointsViewModel) {
    val state by viewModel.state.collectAsState()

    var name by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var interval by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadMetrics()
    }

    Column {
        OutlinedTextField(name, { name = it }, label = { Text("Name") })
        OutlinedTextField(url, { url = it }, label = { Text("URL") })
        OutlinedTextField(interval, { interval = it }, label = { Text("Interval") })

        Button(onClick = {
            viewModel.createEndpoint(name, url, interval)
        }) {
            Text("Create")
        }

        if (state.creating) CircularProgressIndicator()

        state.message?.let { Text(it) }

        Button(onClick = {
            viewModel.loadMetrics()
        }) {
            Text("Update Metrics")
        }

        when {
            state.isLoading -> CircularProgressIndicator()
            state.error != null -> Text("Error: ${state.error}")
            state.endpoints != null -> Text(state.endpoints!!)
        }

        Button(onClick = { viewModel.loadMonitored() }) {
            Text("Get Monitored")
        }

        state.monitoredEndpoints?.let {
            Text(it)
        }
    }
}