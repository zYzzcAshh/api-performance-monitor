package pt.isel.api_pm.repo.exposed

import kotlinx.coroutines.test.runTest
import pt.isel.api_pm.notification.NotificationConfig
import kotlin.test.*

class EndpointRepositoryExposedTests {

    private fun createRepository(): EndpointRepositoryExposed {

        TestDatabase.init()

        return EndpointRepositoryExposed(
            TestDatabase.db
        )
    }

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
                alertRule = null
            )

            val endpoints =
                repository.getByUser(1u)

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
    fun `should return all endpoints`() =
        runTest {

            val repository =
                createRepository()

            repository.add(
                1u,
                "https://api1.com",
                "api1",
                60,
                NotificationConfig.None,
                null
            )

            repository.add(
                2u,
                "https://api2.com",
                "api2",
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
                60,
                NotificationConfig.None,
                null
            )

            val endpoint =
                repository.getByUser(1u).first()

            repository.delete(
                1u,
                endpoint.id
            )

            val endpoints =
                repository.getByUser(1u)

            assertTrue(
                endpoints.isEmpty()
            )
        }

    @Test
    fun `should detect existing url`() =
        runTest {

            val repository =
                createRepository()

            repository.add(
                1u,
                "https://api.github.com/",
                "github",
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
}