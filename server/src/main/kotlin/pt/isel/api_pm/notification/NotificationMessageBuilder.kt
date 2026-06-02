package pt.isel.api_pm.notification

import kotlin.time.Clock

object NotificationMessageBuilder {

    fun build(
        endpointName: String
    ): String {

        return """
🚨 API Performance Monitor

Endpoint:
$endpointName

Status:
Alert Triggered

Timestamp:
${Clock.System.now()}
        """.trimIndent()
    }
}