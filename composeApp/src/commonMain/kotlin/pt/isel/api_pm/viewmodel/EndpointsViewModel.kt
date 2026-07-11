package pt.isel.api_pm.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.api.Api
import pt.isel.api_pm.domain.endpoint.EndpointUiModel
import pt.isel.api_pm.domain.endpoint.HttpMethod
import pt.isel.api_pm.dto.endpoint.CreateEndpointRequest
import pt.isel.api_pm.dto.metric.AggregatedMetric
import pt.isel.api_pm.dto.metric.RequestMetric
import pt.isel.api_pm.notification.NotificationConfig
import pt.isel.api_pm.domain.agent.AgentUiModel
import pt.isel.api_pm.dto.metric.AgentAggregatedMetric
import pt.isel.api_pm.dto.metric.AgentRequestMetric

data class EndpointsState(
    val summary: AggregatedMetric? = null,
    val metrics: List<RequestMetric> = emptyList(),

    val agentSummary: AgentAggregatedMetric? = null,
    val agentMetrics: List<AgentRequestMetric> = emptyList(),
    val agents: List<AgentUiModel> = emptyList(),

    val monitoredEndpoints: List<EndpointUiModel> = emptyList(),

    val isLoading: Boolean = false,
    val error: String? = null,
    val creating: Boolean = false,
    val message: String? = null
)

class EndpointsViewModel(
    private val api: Api,
    private val token: String,
    private val scope: CoroutineScope =
        CoroutineScope(Dispatchers.Default)
) {

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
                    metrics = result.getOrNull() ?: emptyList(),
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

    fun loadAgents() {

        scope.launch {

            _state.value = _state.value.copy(
                isLoading = true
            )

            val result =
                api.getAgents(token)

            _state.value =
                if (result.isSuccess) {

                    _state.value.copy(
                        agents = result.getOrNull() ?: emptyList(),
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

    fun loadAgentMetrics(agentId: UInt) {

        scope.launch {

            val result =
                api.getAgentMetrics(
                    token,
                    agentId
                )

            _state.value =
                if (result.isSuccess) {

                    _state.value.copy(
                        agentMetrics = result.getOrNull() ?: emptyList()
                    )

                } else {

                    _state.value.copy(
                        error = result.exceptionOrNull()?.message
                    )
                }
        }
    }

    fun loadAgentSummary(agentId: UInt) {

        scope.launch {

            val result =
                api.getAgentSummary(
                    token,
                    agentId
                )

            _state.value =
                if (result.isSuccess) {

                    _state.value.copy(
                        agentSummary = result.getOrNull()
                    )

                } else {

                    _state.value.copy(
                        error = result.exceptionOrNull()?.message
                    )
                }
        }
    }

    fun createEndpoint(
        name: String,
        url: String,
        method: HttpMethod,
        interval: String,
        notification: NotificationConfig,
        alertRule: AlertRule?
    ) {

        scope.launch {

            val intervalInt =
                interval.toInt()

            _state.value = _state.value.copy(creating = true, message = null)

            val request = CreateEndpointRequest(
                url = url,
                name = name,
                method = method,
                intervalSeconds = intervalInt.toLong(),
                notification = notification,
                alertRule = alertRule
            )

            println(request)

            val result =
                api.createEndpointMonitor(
                    token,
                    request
                )

            println(result)

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
                    metrics = emptyList()
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

    fun loadSummary(endpointId: UInt) {

        scope.launch {

            val result =
                api.getMetricsSummary(
                    token,
                    endpointId
                )

            _state.value =
                if (result.isSuccess) {

                    _state.value.copy(
                        summary = result.getOrNull()
                    )

                } else {

                    _state.value.copy(
                        error = result.exceptionOrNull()?.message
                    )
                }
        }
    }

    fun stopEndpoint(endpointId: UInt) {
        scope.launch {
            api.stopEndpoint(
                token,
                endpointId
            )
            loadMonitored()
        }
    }

    fun continueEndpoint(endpointId: UInt) {
        scope.launch {
            api.continueEndpoint(
                token,
                endpointId
            )
            loadMonitored()
        }
    }
}