package pt.isel.api_pm.domain.endpoint

import kotlin.jvm.JvmInline

@JvmInline
value class IntervalSeconds(val value: Long) {
    init {
        require(value >= 60) { "Interval must be >= 60 seconds" }
    }
}