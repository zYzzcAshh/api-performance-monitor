package pt.isel.api_pm.alert

import pt.isel.api_pm.dto.metric.RequestMetric

class AlertEvaluator {

    fun shouldTrigger(
        metrics: List<RequestMetric>,
        rule: AlertRule
    ): Boolean {
        if (metrics.isEmpty()) return false

        return when (rule) {

            is AlertRule.LatencyRule -> {

                val values = metrics.map { it.latency }

                when (val agg = rule.aggregation) {

                    is AggregationType.ALL -> {
                        values.all {
                            evaluateCondition(it, rule.operator, rule.value)
                        }
                    }

                    is AggregationType.AVG -> {
                        val avg = values.average().toLong()
                        evaluateCondition(avg, rule.operator, rule.value)
                    }

                    is AggregationType.COUNT -> {
                        val count = values.count {
                            evaluateCondition(it, rule.operator, rule.value)
                        }

                        count >= agg.count
                    }
                }
            }

            is AlertRule.StatusCodeRule -> {

                val values = metrics.map { it.statusCode.toLong() }

                when (val agg = rule.aggregation) {

                    is AggregationType.ALL -> {
                        values.all {
                            evaluateCondition(it, rule.operator, rule.value.toLong())
                        }
                    }

                    is AggregationType.AVG -> {
                        val avg = values.average().toLong()
                        evaluateCondition(avg, rule.operator, rule.value.toLong())
                    }

                    is AggregationType.COUNT -> {
                        val count = values.count {
                            evaluateCondition(it, rule.operator, rule.value.toLong())
                        }

                        count >= agg.count
                    }
                }
            }

            is AlertRule.DownTimeRule -> {

                val failed = metrics.count { it.statusCode >= 500 }

                when (val agg = rule.aggregation) {

                    is AggregationType.ALL -> failed == metrics.size

                    is AggregationType.AVG -> failed.toDouble() / metrics.size >= 0.5

                    is AggregationType.COUNT -> failed >= agg.count
                }
            }
        }
    }
}