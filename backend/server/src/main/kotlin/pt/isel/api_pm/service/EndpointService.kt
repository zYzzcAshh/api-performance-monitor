package pt.isel.api_pm.service

import pt.isel.api_pm.exceptions.DuplicateEndpointException
import pt.isel.api_pm.exceptions.InvalidIntervalException
import pt.isel.api_pm.exceptions.InvalidUrlException
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

        if (repo.getByUser(userId).any { it.url.removeSuffix("/") == normalizedUrl }) {
            throw DuplicateEndpointException(url)
        }

        if (!isValidUrl(url)) {
            throw InvalidUrlException(url)
        }

        if (intervalSeconds < 50) {
            throw InvalidIntervalException(intervalSeconds)
        }

        if (repo.existsByUrlAndUser(userId, normalizedUrl)) {
            throw DuplicateEndpointException(url)
        }

        repo.add(userId, normalizedUrl, name, intervalSeconds)
    }

    suspend fun delete(
        userId: Int,
        monitoredEndpointId: Int,
    ) = repo.delete(userId, monitoredEndpointId)

    private fun isValidUrl(url: String): Boolean {
        return try {
            java.net.URL(url)
            true
        } catch (e: Exception) {
            false
        }
    }

    /* not deprecated version (to be tested)
    private fun isValidUrl(url: String): Boolean {
        return try {
            val uri = java.net.URI(url)
            val scheme = uri.scheme?.lowercase()
            (scheme == "http" || scheme == "https") && !uri.host.isNullOrBlank()
        } catch (e: Exception) {
            false
        }
    }
     */
}
