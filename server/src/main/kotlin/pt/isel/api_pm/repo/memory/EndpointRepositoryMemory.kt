package pt.isel.api_pm.repo.memory

import org.slf4j.LoggerFactory
import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import pt.isel.api_pm.domain.endpoint.HttpMethod
import pt.isel.api_pm.domain.endpoint.IntervalSeconds
import pt.isel.api_pm.domain.endpoint.MonitoredEndpoint
import pt.isel.api_pm.notification.NotificationConfig
import pt.isel.api_pm.repo.EndpointRepository
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Clock

class EndpointRepositoryMemory : EndpointRepository {
    private val logger = LoggerFactory.getLogger(EndpointRepositoryMemory::class.java)

    private val endpoints =
        ConcurrentHashMap<UInt, ConcurrentHashMap<UInt, MonitoredEndpoint>>()

    override suspend fun getAll(): List<MonitoredEndpoint> = endpoints.values.flatMap { it.values }

    override suspend fun getAllByIntervalSeconds(intervalSeconds: IntervalSeconds): List<MonitoredEndpoint> {
        return endpoints.values.flatMap { it.values }.filter { it.interval == intervalSeconds }
    }

    override suspend fun getByUser(userId: UInt): List<MonitoredEndpoint> = endpoints[userId]?.values?.toList() ?: emptyList()

    override suspend fun add(
        userId: UInt,
        url: String,
        name: String,
        method: HttpMethod,
        intervalSeconds: Long,
        notification: NotificationConfig,
        alertRule: AlertRule?
    ) {

        val normalizedUrl = url.removeSuffix("/")

        val monitoredEndpointIdVal =
            endpoints[userId]?.keys?.maxOrNull()

        val monitoredEndpointId =
            if (monitoredEndpointIdVal == null) 0u
            else monitoredEndpointIdVal + 1u

        val monitoredEndpoint =
            MonitoredEndpoint(
                id = monitoredEndpointId,
                userId = userId,
                url = EndpointUrl(normalizedUrl),
                name = name,
                method = method,
                interval = IntervalSeconds(intervalSeconds),
                createdAt = Clock.System.now(),
                notification = notification,
                alertRule = alertRule,
            )

        endpoints
            .getOrPut(userId) { ConcurrentHashMap() }[monitoredEndpoint.id] =
            monitoredEndpoint

        logger.info(
            "Added endpoint: userId=$userId, endpointId=$monitoredEndpointId, url=$normalizedUrl"
        )
    }

    override suspend fun delete(
        userId: UInt,
        monitoredEndpointId: UInt,
    ) {
        endpoints[userId]?.remove(monitoredEndpointId)

        logger.info("Removed endpoint: userId=$userId, endpointId=$monitoredEndpointId")
    }

    override suspend fun existsByUrlAndUser(
        userId: UInt,
        url: String,
    ): Boolean =
        endpoints[userId]?.values?.any {
            it.url.normalized() == url.removeSuffix("/")
        } ?: false
}
