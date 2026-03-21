package pt.isel.api_pm.repo.memory

import pt.isel.api_pm.domain.endpoint.MonitoredEndpoint
import pt.isel.api_pm.repo.EndpointRepository
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Clock

class EndpointRepositoryMemory : EndpointRepository {
    private val endpoints = ConcurrentHashMap<Int, ConcurrentHashMap<Int, MonitoredEndpoint>>()

    override suspend fun getAll(): List<MonitoredEndpoint> = endpoints.values.flatMap { it.values }

    override suspend fun getByUser(userId: Int): List<MonitoredEndpoint> {
        println("getByUser: userId=$userId, result=${endpoints[userId]?.values?.toList()}")

        return endpoints[userId]?.values?.toList() ?: emptyList()
    }

    override suspend fun add(
        userId: Int,
        url: String,
        name: String,
        intervalSeconds: Long,
    ) {
        val monitoredEndpointId = (endpoints[userId]?.keys?.maxOrNull() ?: -1) + 1
        val monitoredEndpoint =
            MonitoredEndpoint(
                monitoredEndpointId,
                userId,
                url,
                name,
                intervalSeconds,
                Clock.System.now(),
            )
        endpoints.getOrPut(userId) { ConcurrentHashMap() }[monitoredEndpoint.id] = monitoredEndpoint

        println("Added endpoint: userId=$userId, endpointId=$monitoredEndpointId, url=$url")
        println("Current endpoints map: $endpoints")
    }

    override suspend fun delete(
        userId: Int,
        monitoredEndpointId: Int,
    ) {
        endpoints[userId]?.remove(monitoredEndpointId)
    }
}
