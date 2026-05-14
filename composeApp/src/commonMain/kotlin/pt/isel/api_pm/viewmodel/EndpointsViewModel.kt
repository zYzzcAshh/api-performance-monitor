package pt.isel.api_pm.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pt.isel.api_pm.alert.AggregationType
import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.alert.ComparisonOperator
import pt.isel.api_pm.api.ApiClient
import pt.isel.api_pm.config.DemoConfig
import pt.isel.api_pm.domain.endpoint.DurationSeconds
import pt.isel.api_pm.model.EndpointUiModel
import pt.isel.api_pm.dto.endpoint.CreateEndpointRequest
import pt.isel.api_pm.notification.NotificationConfig

data class EndpointsState(
    val endpoints: String? = null,
    val monitoredEndpoints: List<EndpointUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val creating: Boolean = false,
    val message: String? = null
)

class EndpointsViewModel(
    private val api: ApiClient,
    private val token: String
) {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val _state = MutableStateFlow(EndpointsState())
    val state: StateFlow<EndpointsState> = _state

    fun loadMetrics(endpointId: UInt) {
        scope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val result = api.getEndpointMetrics(
                token,
                endpointId
            )

            _state.value = if (result.isSuccess) {
                _state.value.copy(
                    endpoints = result.getOrNull(),
                    isLoading = false
                )
            } else {
                _state.value.copy(
                    error = result.exceptionOrNull()?.message,
                    isLoading = false
                )
            }
        }
    }

    fun loadMonitored() {

        scope.launch {

            _state.value = _state.value.copy(
                isLoading = true
            )

            val result = api.getEndpoints(token)

            _state.value =
                if (result.isSuccess) {

                    _state.value.copy(
                        monitoredEndpoints = result.getOrNull() ?: emptyList(),
                        isLoading = false
                    )

                } else {

                    _state.value.copy(
                        error = result.exceptionOrNull()?.message,
                        isLoading = false
                    )
                }
        }
    }

    fun createEndpoint(name: String, url: String, interval: String) {
        scope.launch {
            val intervalInt = interval.toIntOrNull()
            if (name.isBlank() || url.isBlank() || intervalInt == null || intervalInt <= 0) {
                _state.value = _state.value.copy(message = "Invalid input")
                return@launch
            }

            _state.value = _state.value.copy(creating = true, message = null)

            val request = CreateEndpointRequest(
                url = url,
                name = name,
                intervalSeconds = intervalInt.toLong(),

                notification = NotificationConfig.DiscordWebhook(
                    webhookUrl = DemoConfig.DISCORD_WEBHOOK
                ),

                alertRule = AlertRule.StatusCodeRule(
                    operator = ComparisonOperator.GTE,
                    value = 500,
                    durationSeconds = DurationSeconds(60),
                    aggregation = AggregationType.COUNT(1)
                )
            )

            val result = api.createEndpointMonitor(
                token,
                request
            )

            _state.value = if (result.isSuccess) {

                loadMonitored()

                _state.value.copy(
                    message = "Created successfully",
                    creating = false
                )
            } else {
                _state.value.copy(
                    message = "Error: ${result.exceptionOrNull()?.message}",
                    creating = false
                )
            }
        }
    }

    fun deleteEndpoint(endpointId: UInt) {

        scope.launch {

            val result = api.deleteEndpoint(
                token,
                endpointId
            )

            if (result.isSuccess) {

                _state.value = _state.value.copy(
                    endpoints = null
                )

                loadMonitored()

                _state.value = _state.value.copy(
                    message = "Endpoint deleted"
                )

            } else {

                _state.value = _state.value.copy(
                    message = "Error deleting endpoint"
                )
            }
        }
    }
}