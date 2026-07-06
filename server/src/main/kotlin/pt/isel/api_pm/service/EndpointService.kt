package pt.isel.api_pm.service

import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import pt.isel.api_pm.domain.endpoint.HttpMethod
import pt.isel.api_pm.domain.endpoint.INTERVAL_SECONDS_LIST
import pt.isel.api_pm.domain.endpoint.IntervalSeconds
import pt.isel.api_pm.exceptions.DuplicateEndpointException
import pt.isel.api_pm.exceptions.InvalidIntervalException
import pt.isel.api_pm.notification.NotificationConfig
import pt.isel.api_pm.repo.EndpointRepository

class EndpointService(
    private val repo: EndpointRepository,
) {
    suspend fun getAll() = repo.getAll()

    suspend fun getAllByIntervalSeconds(intervalSeconds: IntervalSeconds) = repo.getAllByIntervalSeconds(intervalSeconds)

    suspend fun getAllActiveByIntervalSeconds(intervalSeconds: IntervalSeconds) = repo.getAllActiveByIntervalSeconds(intervalSeconds)

    suspend fun getByUser(userId: UInt) = repo.getByUser(userId)

    suspend fun add(
        userId: UInt,
        url: EndpointUrl,
        name: String,
        method: HttpMethod,
        interval: IntervalSeconds,
        notification: NotificationConfig,
        alertRule: AlertRule?
    ) {
        val normalizedUrl = url.normalized()

        if (interval.value !in INTERVAL_SECONDS_LIST) {
            throw InvalidIntervalException(interval.value)
        }

        if (repo.existsByUrlAndUser(userId, normalizedUrl)) {
            throw DuplicateEndpointException(normalizedUrl)
        }

        repo.add(userId, normalizedUrl, name, method, interval.value, notification, alertRule)
    }

    suspend fun stopMonitoring(
        userId: UInt,
        monitoredEndpointId: UInt,
    ) = repo.stopMonitoring(userId, monitoredEndpointId)

    suspend fun continueMonitoring(
        userId: UInt,
        monitoredEndpointId: UInt,
    ) = repo.continueMonitoring(userId, monitoredEndpointId)

    suspend fun delete(
        userId: UInt,
        monitoredEndpointId: UInt,
    ) = repo.delete(userId, monitoredEndpointId)
}
