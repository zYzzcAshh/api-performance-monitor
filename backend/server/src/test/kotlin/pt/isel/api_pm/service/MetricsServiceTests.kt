package pt.isel.api_pm.service

import kotlin.test.*
import kotlinx.coroutines.runBlocking
import pt.isel.api_pm.domain.metric.RequestMetric
import pt.isel.api_pm.repo.memory.MetricsRepositoryMemory
import kotlin.time.Clock.System.now

class MetricsServiceTests {

    @Test
    fun `should calculate metrics summary`() = runBlocking {
        val repo = MetricsRepositoryMemory()
        val service = MetricsService(repo)

        val userId = 0
        val endpointId = 0

        repo.save(userId, endpointId, RequestMetric("url", now(), 100, 200))
        repo.save(userId, endpointId, RequestMetric("url", now(), 200, 200))
        repo.save(userId, endpointId, RequestMetric("url", now(), 300, 500))

        val summary = service.getSummary(userId, endpointId)

        assertEquals(3, summary.totalRequests)
        assertEquals(66.666, summary.uptime, 0.1)
        assertTrue(summary.averageLatency > 0)
    }

    @Test
    fun `should return empty summary when no metrics`() = runBlocking {
        val repo = MetricsRepositoryMemory()
        val service = MetricsService(repo)

        val summary = service.getSummary(0, 0)

        assertEquals(0, summary.totalRequests)
        assertEquals(0.0, summary.uptime)
        assertEquals(0.0, summary.averageLatency)
    }

    @Test
    fun `should calculate 100 percent uptime`() = runBlocking {
        val repo = MetricsRepositoryMemory()
        val service = MetricsService(repo)

        val userId = 0
        val endpointId = 0

        repo.save(userId, endpointId, RequestMetric("url", now(), 100, 200))
        repo.save(userId, endpointId, RequestMetric("url", now(), 200, 201))

        val summary = service.getSummary(userId, endpointId)

        assertEquals(100.0, summary.uptime)
    }
}
