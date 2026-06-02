package pt.isel.api_pm.notification

class LogNotificationSender : NotificationSender {

    override suspend fun send(
        endpointName: String
    ) {

        println(
            "Alert triggered for monitored endpoint $endpointName!"
        )
    }
}