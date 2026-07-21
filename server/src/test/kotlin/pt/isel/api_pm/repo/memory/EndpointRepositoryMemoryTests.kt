package pt.isel.api_pm.repo.memory

import kotlinx.coroutines.test.runTest
import pt.isel.api_pm.domain.endpoint.HttpMethod
import pt.isel.api_pm.notification.NotificationConfig
import kotlin.test.*

class EndpointRepositoryMemoryTests {

    private fun createRepository() =
        EndpointRepositoryMemory()

    @Test
    fun `should add endpoint`() =
        runTest {

            val repository =
                createRepository()

            repository.add(
                userId = 1u,
                url = "https://api.github.com",
                name = "github",
                intervalSeconds = 60,
                notification = NotificationConfig.None,
                alertRule = null,
                method = HttpMethod.GET
            )

            val endpoints =
                repository.getByUser(1u)

            assertEquals(
                1,
                endpoints.size
            )

            val endpoint =
                endpoints.first()

            assertEquals(
                0u,
                endpoint.id
            )

            assertEquals(
                "github",
                endpoint.name
            )

            assertEquals(
                "https://api.github.com",
                endpoint.url.value
            )
        }

    @Test
    fun `should generate incremental endpoint ids`() =
        runTest {

            val repository =
                createRepository()

            repository.add(
                1u,
                "https://api1.com",
                "api1",
                HttpMethod.GET,
                60,
                NotificationConfig.None,
                null
            )

            repository.add(
                1u,
                "https://api2.com",
                "api2",
                HttpMethod.GET,
                60,
                NotificationConfig.None,
                null
            )

            val endpoints =
                repository.getByUser(1u)

            assertEquals(
                0u,
                endpoints[0].id
            )

            assertEquals(
                1u,
                endpoints[1].id
            )
        }

    @Test
    fun `should return endpoints by user`() =
        runTest {

            val repository =
                createRepository()

            repository.add(
                1u,
                "https://user1-api.com",
                "u1",
                HttpMethod.GET,
                60,
                NotificationConfig.None,
                null
            )

            repository.add(
                2u,
                "https://user2-api.com",
                "u2",
                HttpMethod.GET,
                60,
                NotificationConfig.None,
                null
            )

            val user1Endpoints =
                repository.getByUser(1u)

            val user2Endpoints =
                repository.getByUser(2u)

            assertEquals(
                1,
                user1Endpoints.size
            )

            assertEquals(
                1,
                user2Endpoints.size
            )

            assertEquals(
                "u1",
                user1Endpoints.first().name
            )

            assertEquals(
                "u2",
                user2Endpoints.first().name
            )
        }

    @Test
    fun `should return all endpoints`() =
        runTest {

            val repository =
                createRepository()

            repository.add(
                1u,
                "https://api1.com",
                "api1",
                HttpMethod.GET,
                60,
                NotificationConfig.None,
                null
            )

            repository.add(
                2u,
                "https://api2.com",
                "api2",
                HttpMethod.GET,
                60,
                NotificationConfig.None,
                null
            )

            val endpoints =
                repository.getAll()

            assertEquals(
                2,
                endpoints.size
            )
        }

    @Test
    fun `should delete endpoint`() =
        runTest {

            val repository =
                createRepository()

            repository.add(
                1u,
                "https://api.com",
                "api",
                HttpMethod.GET,
                60,
                NotificationConfig.None,
                null
            )

            repository.delete(
                1u,
                0u
            )

            val endpoints =
                repository.getByUser(1u)

            assertTrue(
                endpoints.isEmpty()
            )
        }

    @Test
    fun `should detect existing url for user`() =
        runTest {

            val repository =
                createRepository()

            repository.add(
                1u,
                "https://api.github.com",
                "github",
                HttpMethod.GET,
                60,
                NotificationConfig.None,
                null
            )

            val exists =
                repository.existsByUrlAndUser(
                    1u,
                    "https://api.github.com"
                )

            assertTrue(exists)
        }

    @Test
    fun `should normalize urls when checking duplicates`() =
        runTest {

            val repository =
                createRepository()

            repository.add(
                1u,
                "https://api.github.com/",
                "github",
                HttpMethod.GET,
                60,
                NotificationConfig.None,
                null
            )

            val exists =
                repository.existsByUrlAndUser(
                    1u,
                    "https://api.github.com"
                )

            assertTrue(exists)
        }

    @Test
    fun `should return false for missing url`() =
        runTest {

            val repository =
                createRepository()

            val exists =
                repository.existsByUrlAndUser(
                    1u,
                    "https://missing.com"
                )

            assertFalse(exists)
        }
}