package pt.isel.api_pm.repo.exposed

import kotlinx.coroutines.test.runTest
import kotlin.test.*
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import pt.isel.api_pm.database.tables.UserTable
import pt.isel.api_pm.notification.NotificationConfig
import kotlin.time.Clock

class EndpointRepositoryExposedTests {

    private fun createRepository(): EndpointRepositoryExposed {

        TestDatabase.init()

        return EndpointRepositoryExposed(
            TestDatabase.db
        )
    }

    private fun createTestUser(id: Int) {

        transaction(TestDatabase.db) {

            UserTable.insert {
                it[UserTable.id] = id
                it[username] = "user$id"
                it[passwordhash] = "hash"
                it[createdAt] = Clock.System.now()
            }
        }
    }

    @Test
    fun `should add endpoint`() =
        runTest {

            val repository =
                createRepository()

            createTestUser(1)

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

            createTestUser(1)
            createTestUser(2)

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

            createTestUser(1)

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

            createTestUser(1)

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