package pt.isel.api_pm.domain.endpoint

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class EndpointUrl(val value: String) {
    init {
        val normalized = value.removeSuffix("/")

        require(normalized.startsWith("http://") || normalized.startsWith("https://")) {
            "URL must start with http:// or https://"
        }

        require(normalized.length > 10) {
            "URL is too short"
        }

        require(!normalized.contains(" ")) {
            "URL must not contain spaces"
        }
    }

    fun normalized(): String = value.removeSuffix("/")
}