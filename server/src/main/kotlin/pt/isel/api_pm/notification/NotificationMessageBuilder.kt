package pt.isel.api_pm.notification

import kotlin.time.Clock

object NotificationMessageBuilder {

    fun build(
        endpointName: String
    ): String {
        val timestamp = Clock.System.now()
        return "Alert: Endpoint '$endpointName' is at $timestamp"
    }
}