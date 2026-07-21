package pt.isel.api_pm.service

import kotlinx.coroutines.test.runTest
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import pt.isel.api_pm.domain.metrics.EndpointMetrics
import pt.isel.api_pm.dto.metric.RequestMetric
import pt.isel.api_pm.repo.memory.MetricsRepositoryMemory
import kotlin.test.*
import kotlin.time.Clock

class MetricsServiceTests {

    private val repository =
        MetricsRepositoryMemory()

    private val service =
        MetricsService(repository)

    private fun successMetric(
        latency: Long = 100
    ) =
        EndpointMetrics(
            endpoint = EndpointUrl("https://api.github.com"),
            timestamp = Clock.System.now(),
            latency = 60,
            statusCode = 200
        )

    private fun failedMetric(
        latency: Long = 500
    ) =
        EndpointMetrics(
            endpoint = EndpointUrl("https://api.github.com"),
            timestamp = Clock.System.now(),
            latency = 60,
            statusCode = 200
        )

    @Test
    fun `should save metric`() =
        runTest {

            service.save(
                userId = 1u,
                monitoredEndpointId = 1u,
                metric = successMetric()
            )

            val metrics =
                service.getByEndpoint(
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

            service.save(
                1u,
                1u,
                successMetric()
            )

            service.save(
                1u,
                2u,
                successMetric()
            )

            val endpoint1Metrics =
                service.getByEndpoint(
                    1u,
                    1u
                )

            val endpoint2Metrics =
                service.getByEndpoint(
                    1u,
                    2u
                )

            assertEquals(
                1,
                endpoint1Metrics.size
            )

            assertEquals(
                1,
                endpoint2Metrics.size
            )
        }

    @Test
    fun `should calculate summary correctly`() =
        runTest {

            service.save(
                1u,
                1u,
                successMetric(100)
            )

            service.save(
                1u,
                1u,
                successMetric(200)
            )

            service.save(
                1u,
                1u,
                failedMetric(300)
            )

            val summary =
                service.getSummary(
                    1u,
                    1u
                )

            assertEquals(
                3,
                summary.totalRequests
            )

            assertEquals(
                66.66666666666666,
                summary.uptime,
                0.01
            )

            assertEquals(
                200.0,
                summary.averageLatency
            )
        }

    @Test
    fun `should return empty summary when no metrics exist`() =
        runTest {

            val summary =
                service.getSummary(
                    1u,
                    999u
                )

            assertEquals(
                0,
                summary.totalRequests
            )

            assertEquals(
                0.0,
                summary.uptime,
                0.01
            )

            assertEquals(
                0.0,
                summary.averageLatency
            )
        }

    @Test
    fun `should return all metrics`() =
        runTest {

            service.save(
                1u,
                1u,
                successMetric()
            )

            service.save(
                1u,
                2u,
                failedMetric()
            )

            val metrics =
                service.getAll()

            assertEquals(
                2,
                metrics.size
            )
        }
}