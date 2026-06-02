package pt.isel.api_pm.api

import pt.isel.api_pm.domain.endpoint.EndpointUiModel
import pt.isel.api_pm.dto.endpoint.CreateEndpointRequest
import pt.isel.api_pm.dto.metric.AggregatedMetric
import pt.isel.api_pm.dto.metric.RequestMetric

class FakeApi : Api {

    var loginResult: Result<String> =
        Result.success("token")

    var registerResult: Result<String> =
        Result.success("registered")

    var endpointsResult: Result<List<EndpointUiModel>> =
        Result.success(emptyList())

    var createEndpointResult: Result<String> =
        Result.success("created")

    var metricsResult: Result<List<RequestMetric>> =
        Result.success(emptyList())

    var deleteEndpointResult: Result<Unit> =
        Result.success(Unit)

    var summaryResult: Result<AggregatedMetric> =
        Result.failure(
            NotImplementedError("Configure summaryResult in test")
        )

    override suspend fun login(
        username: String,
        password: String
    ) = loginResult

    override suspend fun register(
        username: String,
        password: String
    ) = registerResult

    override suspend fun getEndpoints(
        token: String
    ) = endpointsResult

    override suspend fun createEndpointMonitor(
        token: String,
        request: CreateEndpointRequest
    ) = createEndpointResult

    override suspend fun getEndpointMetrics(
        token: String,
        endpointId: UInt
    ) = metricsResult

    override suspend fun deleteEndpoint(
        token: String,
        endpointId: UInt
    ) = deleteEndpointResult

    override suspend fun getMetricsSummary(
        token: String,
        endpointId: UInt
    ) = summaryResult
}