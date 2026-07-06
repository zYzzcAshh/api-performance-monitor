package pt.isel.api_pm.alert

import pt.isel.api_pm.domain.metrics.AgentEndpointMetrics
import pt.isel.api_pm.domain.metrics.EndpointMetrics

class AlertEvaluator {
    private data class MetricSample(
        val latency: Long,
        val statusCode: Int
    )

    fun shouldTrigger(metrics: List<EndpointMetrics>, rule: AlertRule): Boolean {
        return evaluate(metrics.map { MetricSample(it.latency, it.statusCode) }, rule)
    }

    fun shouldTriggerAgent(metrics: List<AgentEndpointMetrics>, rule: AlertRule): Boolean {
        return evaluate(metrics.map { MetricSample(it.latency, it.statusCode) }, rule)
    }

    private fun evaluate(samples: List<MetricSample>, rule: AlertRule): Boolean {
        if (samples.isEmpty()) return false

        return when (rule) {

            is AlertRule.LatencyRule -> {
                val values = samples.map { it.latency }
                matchesAggregation(values, rule.aggregation) {
                    evaluateCondition(it, rule.operator, rule.value)
                }
            }

            is AlertRule.StatusCodeRule -> {
                val values = samples.map { it.statusCode.toLong() }
                matchesAggregation(values, rule.aggregation) {
                    evaluateCondition(it, rule.operator, rule.value.toLong())
                }
            }

            is AlertRule.DownTimeRule -> {
                val failed = samples.count { it.statusCode >= 500 }

                when (val agg = rule.aggregation) {
                    is AggregationType.ALL -> failed == samples.size
                    is AggregationType.AVG -> failed.toDouble() / samples.size >= 0.5
                    is AggregationType.COUNT -> failed >= agg.count
                }
            }
        }
    }

    private fun matchesAggregation(
        values: List<Long>,
        aggregation: AggregationType,
        condition: (Long) -> Boolean
    ): Boolean {
        return when (aggregation) {
            is AggregationType.ALL -> values.all(condition)
            is AggregationType.AVG -> condition(values.average().toLong())
            is AggregationType.COUNT -> values.count(condition) >= aggregation.count
        }
    }
}