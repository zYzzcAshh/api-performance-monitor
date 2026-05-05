package pt.isel.api_pm.alert

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class AlertRule {
    abstract val durationSeconds: Long
    abstract val aggregation: AggregationType

    @Serializable
    @SerialName("status_code")
    data class StatusCodeRule(
        val operator: ComparisonOperator,
        val value: Int,
        override val durationSeconds: Long,
        override val aggregation: AggregationType
    ) : AlertRule()

    @Serializable
    @SerialName("latency")
    data class LatencyRule(
        val operator: ComparisonOperator,
        val value: Long,
        override val durationSeconds: Long,
        override val aggregation: AggregationType
    ) : AlertRule()

    @Serializable
    @SerialName("down_time")
    data class DownTimeRule(
        override val durationSeconds: Long,
        override val aggregation: AggregationType = AggregationType.ALL
    ) : AlertRule()
}