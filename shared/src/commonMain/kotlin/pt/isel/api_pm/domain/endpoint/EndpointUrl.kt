package pt.isel.api_pm.domain.endpoint

import kotlinx.serialization.Serializable
import pt.isel.api_pm.exceptions.InvalidUrlException
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class EndpointUrl(val value: String) {

    init {

        val normalized =
            value.removeSuffix("/")

        if (
            !normalized.startsWith("http://") &&
            !normalized.startsWith("https://")
        ) {
            throw InvalidUrlException(value)
        }

        if (normalized.length <= 10) {
            throw InvalidUrlException(value)
        }

        if (normalized.contains(" ")) {
            throw InvalidUrlException(value)
        }
    }

    fun normalized(): String =
        value.removeSuffix("/")
}