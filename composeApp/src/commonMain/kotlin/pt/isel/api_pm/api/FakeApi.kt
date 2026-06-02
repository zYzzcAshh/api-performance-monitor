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
    ) = Result.success(emptyList<EndpointUiModel>())

    override suspend fun createEndpointMonitor(
        token: String,
        request: CreateEndpointRequest
    ) = Result.success("created")

    override suspend fun getEndpointMetrics(
        token: String,
        endpointId: UInt
    ) = Result.success(emptyList<RequestMetric>())

    override suspend fun deleteEndpoint(
        token: String,
        endpointId: UInt
    ) = Result.success(Unit)

    override suspend fun getMetricsSummary(
        token: String,
        endpointId: UInt
    ): Result<AggregatedMetric> {
        throw NotImplementedError()
    }
}