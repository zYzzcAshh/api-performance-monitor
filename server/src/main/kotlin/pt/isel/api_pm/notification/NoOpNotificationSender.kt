package pt.isel.api_pm.notification

class NoOpNotificationSender : NotificationSender {

    override suspend fun send(
        endpointName: String
    ) = Unit
}