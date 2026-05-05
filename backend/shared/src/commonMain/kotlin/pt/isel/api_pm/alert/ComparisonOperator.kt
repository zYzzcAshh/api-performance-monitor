package pt.isel.api_pm.alert

enum class ComparisonOperator {
    GT,
    GTE,
    LT,
    LTE,
    EQ
}

fun evaluateCondition(value: Long, op: ComparisonOperator, target: Long): Boolean {
    return when (op) {
        ComparisonOperator.EQ -> value == target
        ComparisonOperator.GT -> value > target
        ComparisonOperator.GTE -> value >= target
        ComparisonOperator.LT -> value < target
        ComparisonOperator.LTE -> value <= target
    }
}