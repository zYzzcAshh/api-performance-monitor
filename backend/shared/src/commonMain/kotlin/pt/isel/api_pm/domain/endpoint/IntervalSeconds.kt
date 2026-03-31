package pt.isel.api_pm.domain.endpoint

import kotlin.jvm.JvmInline

val INTERVAL_SECONDS_LIST = listOf(60L, 120L, 180L, 300L, 600L, 900L, 1200L, 1800L)

@JvmInline
value class IntervalSeconds(val value: Long) {
    init {
        require(INTERVAL_SECONDS_LIST.contains(value)) { "Interval must be (60, 120, 180, 300, 600, 900, 1200, 1800) seconds" }
    }
}