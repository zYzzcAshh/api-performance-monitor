package pt.isel.api_pm.notification

interface NotificationSender {

    suspend fun send(
        endpointName: String
    )
}