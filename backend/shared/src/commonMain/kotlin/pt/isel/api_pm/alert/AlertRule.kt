package pt.isel.api_pm.alert

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class AlertRule {

    @Serializable
    @SerialName("status_code")
    data class StatusCodeRule(
        val operator: ComparisonOperator,
        val value: Int,
        val durationSeconds: Long,
        val aggregation: AggregationType
    ) : AlertRule()

    @Serializable
    @SerialName("latency")
    data class LatencyRule(
        val operator: ComparisonOperator,
        val value: Long,
        val durationSeconds: Long,
        val aggregation: AggregationType
    ) : AlertRule()

    @Serializable
    @SerialName("down_time")
    data class DownTimeRule(
        val durationSeconds: Long,
        val aggregation: AggregationType = AggregationType.ALL
    ) : AlertRule()
}