package pt.isel.api_pm.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class AgentMessage {
    @Serializable
    @SerialName("metrics")
    data class Metrics(
        val endpointName: String,
        val statusCode: Int,
        val responseTimeMs: Long,
        val timestamp: Long,
    ) : AgentMessage()

    @Serializable
    @SerialName("error")
    data class Error(val endpointName: String, val message: String) : AgentMessage()
}