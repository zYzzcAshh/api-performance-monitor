package pt.isel.api_pm.api

import pt.isel.api_pm.domain.endpoint.EndpointUiModel
import pt.isel.api_pm.dto.endpoint.CreateEndpointRequest
import pt.isel.api_pm.dto.metric.AggregatedMetric
import pt.isel.api_pm.dto.metric.RequestMetric

interface Api {

    suspend fun register(
        username: String,
        password: String
    ): Result<String>

    suspend fun login(
        username: String,
        password: String
    ): Result<String>

    suspend fun getEndpoints(
        token: String
    ): Result<List<EndpointUiModel>>

    suspend fun createEndpointMonitor(
        token: String,
        request: CreateEndpointRequest
    ): Result<String>

    suspend fun getEndpointMetrics(
        token: String,
        endpointId: UInt
    ): Result<List<RequestMetric>>

    suspend fun deleteEndpoint(
        token: String,
        endpointId: UInt
    ): Result<Unit>

    suspend fun getMetricsSummary(
        token: String,
        endpointId: UInt
    ): Result<AggregatedMetric>
}