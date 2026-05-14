package pt.isel.api_pm.domain.endpoint

import kotlinx.serialization.Serializable
import pt.isel.api_pm.exceptions.DurationValueException
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class DurationSeconds(val value: Long) {
    init {
        if (value <= 0 || value > 7 * 24 * 3600) {
            throw DurationValueException(value)
        }
    }
}