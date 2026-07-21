package pt.isel.api_pm.repo.memory

import kotlinx.coroutines.test.runTest
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import pt.isel.api_pm.domain.metrics.EndpointMetrics
import pt.isel.api_pm.dto.metric.RequestMetric
import kotlin.test.*
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes

class MetricsRepositoryMemoryTests {

    private fun createRepository() =
        MetricsRepositoryMemory()

    private fun metric(
        latency: Long = 100,
        statusCode: Int = 200,
        timestamp: kotlin.time.Instant = Clock.System.now()
    ) =
        RequestMetric(
            endpoint = EndpointUrl("https://api.github.com"),
            timestamp = timestamp,
            latency = latency,
            statusCode = statusCode
        )

    @Test
    fun `should save metric`() =
        runTest {

            val repository =
                createRepository()

            repository.save(
                userId = 1u,
                monitoredEndpointId = 1u,
                metric = EndpointMetrics(
                    endpoint = EndpointUrl("https://api.github.com"),
                    timestamp = Clock.System.now(),
                    latency = 60,
                    statusCode = 200
                )
            )

            val metrics =
                repository.getByEndpoint(
                    1u,
                    1u
                )

            assertEquals(
                1,
                metrics.size
            )
        }

    @Test
    fun `should return metrics by endpoint`() =
        runTest {

            val repository =
                createRepository()

            repository.save(
                1u,
                1u,
                EndpointMetrics(
                    endpoint = EndpointUrl("https://api.github.com"),
                    timestamp = Clock.System.now(),
                    latency = 60,
                    statusCode = 200
                )
            )

            repository.save(
                1u,
                2u,
                EndpointMetrics(
                    endpoint = EndpointUrl("https://api.github.com"),
                    timestamp = Clock.System.now(),
                    latency = 60,
                    statusCode = 200
                )
            )

            val endpoint1Metrics =
                repository.getByEndpoint(
                    1u,
                    1u
                )

            val endpoint2Metrics =
                repository.getByEndpoint(
                    1u,
                    2u
                )

            assertEquals(
                1,
                endpoint1Metrics.size
            )

            assertEquals(
                100,
                endpoint1Metrics.first().latency
            )

            assertEquals(
                1,
                endpoint2Metrics.size
            )

            assertEquals(
                200,
                endpoint2Metrics.first().latency
            )
        }

    @Test
    fun `should return all metrics`() =
        runTest {

            val repository =
                createRepository()

            repository.save(
                1u,
                1u,
                EndpointMetrics(
                    endpoint = EndpointUrl("https://api.github.com"),
                    timestamp = Clock.System.now(),
                    latency = 60,
                    statusCode = 200
                )
            )

            repository.save(
                2u,
                2u,
                EndpointMetrics(
                    endpoint = EndpointUrl("https://api.github.com"),
                    timestamp = Clock.System.now(),
                    latency = 60,
                    statusCode = 200
                )
            )

            val metrics =
                repository.getAll()

            assertEquals(
                2,
                metrics.size
            )
        }

    @Test
    fun `should return empty list for missing endpoint`() =
        runTest {

            val repository =
                createRepository()

            val metrics =
                repository.getByEndpoint(
                    1u,
                    999u
                )

            assertTrue(
                metrics.isEmpty()
            )
        }

    @Test
    fun `should return metrics inside interval`() =
        runTest {

            val repository =
                createRepository()

            val now =
                Clock.System.now()

            val oldMetric =
                EndpointMetrics(
                    endpoint = EndpointUrl("https://api.github.com"),
                    timestamp = Clock.System.now(),
                    latency = 60,
                    statusCode = 200
                )

            val recentMetric =
                EndpointMetrics(
                    endpoint = EndpointUrl("https://api.github.com"),
                    timestamp = Clock.System.now(),
                    latency = 60,
                    statusCode = 200
                )

            repository.save(
                1u,
                1u,
                oldMetric
            )

            repository.save(
                1u,
                1u,
                recentMetric
            )

            val metrics =
                repository.getByInterval(
                    userId = 1u,
                    monitoredEndpointId = 1u,
                    from = now.minus(1.minutes),
                    to = now.plus(1.minutes)
                )

            assertEquals(
                1,
                metrics.size
            )

            assertEquals(
                recentMetric.timestamp,
                metrics.first().timestamp
            )
        }

    @Test
    fun `should isolate metrics by user`() =
        runTest {

            val repository =
                createRepository()

            repository.save(
                1u,
                1u,
                EndpointMetrics(
                    endpoint = EndpointUrl("https://api.github.com"),
                    timestamp = Clock.System.now(),
                    latency = 60,
                    statusCode = 200
                )
            )

            repository.save(
                2u,
                1u,
                EndpointMetrics(
                    endpoint = EndpointUrl("https://api.github.com"),
                    timestamp = Clock.System.now(),
                    latency = 60,
                    statusCode = 200
                )
            )

            val user1Metrics =
                repository.getByEndpoint(
                    1u,
                    1u
                )

            val user2Metrics =
                repository.getByEndpoint(
                    2u,
                    1u
                )

            assertEquals(
                100,
                user1Metrics.first().latency
            )

            assertEquals(
                200,
                user2Metrics.first().latency
            )
        }
}