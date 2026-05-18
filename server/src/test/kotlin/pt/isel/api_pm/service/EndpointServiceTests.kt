package pt.isel.api_pm.service

import kotlinx.coroutines.test.runTest
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import pt.isel.api_pm.domain.endpoint.IntervalSeconds
import pt.isel.api_pm.exceptions.DuplicateEndpointException
import pt.isel.api_pm.exceptions.InvalidIntervalException
import pt.isel.api_pm.notification.NotificationConfig
import pt.isel.api_pm.repo.memory.EndpointRepositoryMemory
import kotlin.test.*

class EndpointServiceTests {

    private val repository =
        EndpointRepositoryMemory()

    private val service =
        EndpointService(repository)

    private val notification =
        NotificationConfig.None

    @Test
    fun `should add endpoint`() =
        runTest {

            service.add(
                userId = 1u,
                url = EndpointUrl("https://api.github.com"),
                name = "github",
                interval = IntervalSeconds(60),
                notification = notification,
                alertRule = null
            )

            val endpoints =
                service.getByUser(1u)

            assertEquals(
                1,
                endpoints.size
            )

            assertEquals(
                "github",
                endpoints.first().name
            )
        }

    @Test
    fun `should reject duplicate endpoint`() =
        runTest {

            service.add(
                userId = 1u,
                url = EndpointUrl("https://api.github.com"),
                name = "github",
                interval = IntervalSeconds(60),
                notification = notification,
                alertRule = null
            )

            assertFailsWith<DuplicateEndpointException> {

                service.add(
                    userId = 1u,
                    url = EndpointUrl("https://api.github.com"),
                    name = "github2",
                    interval = IntervalSeconds(60),
                    notification = notification,
                    alertRule = null
                )
            }
        }

    @Test
    fun `should reject invalid interval`() =
        runTest {
            assertFailsWith<IllegalArgumentException> {
                IntervalSeconds(5)
            }
        }

    @Test
    fun `should delete endpoint`() =
        runTest {

            service.add(
                userId = 1u,
                url = EndpointUrl("https://api.github.com"),
                name = "github",
                interval = IntervalSeconds(60),
                notification = notification,
                alertRule = null
            )

            service.delete(
                1u,
                0u
            )

            val endpoints =
                service.getByUser(1u)

            assertTrue(
                endpoints.isEmpty()
            )
        }

    @Test
    fun `should return endpoints by user`() =
        runTest {

            service.add(
                userId = 1u,
                url = EndpointUrl("https://api.github.com"),
                name = "github",
                interval = IntervalSeconds(60),
                notification = notification,
                alertRule = null
            )

            service.add(
                userId = 2u,
                url = EndpointUrl("https://google.com"),
                name = "google",
                interval = IntervalSeconds(60),
                notification = notification,
                alertRule = null
            )

            val user1Endpoints =
                service.getByUser(1u)

            val user2Endpoints =
                service.getByUser(2u)

            assertEquals(
                1,
                user1Endpoints.size
            )

            assertEquals(
                1,
                user2Endpoints.size
            )

            assertEquals(
                "github",
                user1Endpoints.first().name
            )

            assertEquals(
                "google",
                user2Endpoints.first().name
            )
        }
}