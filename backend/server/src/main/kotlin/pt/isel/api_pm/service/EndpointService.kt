package pt.isel.api_pm.service

import pt.isel.api_pm.app.module.INTERVAL_SECONDS_LIST
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import pt.isel.api_pm.domain.endpoint.IntervalSeconds
import pt.isel.api_pm.exceptions.DuplicateEndpointException
import pt.isel.api_pm.exceptions.InvalidIntervalException
import pt.isel.api_pm.repo.EndpointRepository

class EndpointService(
    private val repo: EndpointRepository,
) {
    suspend fun getAll() = repo.getAll()

    suspend fun getByUser(userId: UInt) = repo.getByUser(userId)

    suspend fun add(
        userId: UInt,
        url: EndpointUrl,
        name: String,
        interval: IntervalSeconds,
    ) {
        val normalizedUrl = url.normalized()

        if (interval.value !in INTERVAL_SECONDS_LIST) {
            throw InvalidIntervalException(interval.value)
        }

        if (repo.existsByUrlAndUser(userId, normalizedUrl)) {
            throw DuplicateEndpointException(normalizedUrl)
        }

        repo.add(userId, normalizedUrl, name, interval.value)
    }

    suspend fun delete(
        userId: UInt,
        monitoredEndpointId: UInt,
    ) = repo.delete(userId, monitoredEndpointId)
}