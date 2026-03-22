package pt.isel.api_pm.utils

import java.net.URI

fun isValidUrl(url: String): Boolean {
    return try {
        val uri = URI(url)
        uri.scheme in listOf("http", "https") && uri.host != null && uri.host.isNotEmpty()
    } catch (_: Exception) {
        false
    }
}