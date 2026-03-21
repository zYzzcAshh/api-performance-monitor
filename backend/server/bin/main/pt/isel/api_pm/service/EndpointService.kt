package pt.isel.api_pm.service

import pt.isel.api_pm.exceptions.DuplicateEndpointException
import pt.isel.api_pm.repo.EndpointRepository

class EndpointService(
    private val repo: EndpointRepository,
) {
    suspend fun getAll() = repo.getAll()

    suspend fun getByUser(userId: Int) = repo.getByUser(userId)

    suspend fun add(
        userId: Int,
        url: String,
        name: String,
        intervalSeconds: Long,
    ) {
        val normalizedUrl = url.removeSuffix("/")

        if (repo.getByUser(userId).any { it.url.removeSuffix("/") == normalizedUrl }) throw DuplicateEndpointException(url)

        repo.add(userId, normalizedUrl, name, intervalSeconds)
    }

    suspend fun delete(
        userId: Int,
        monitoredEndpointId: Int,
    ) = repo.delete(userId, monitoredEndpointId)
}
