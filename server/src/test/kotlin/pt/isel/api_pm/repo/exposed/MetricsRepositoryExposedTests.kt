package pt.isel.api_pm.repo.exposed

import kotlinx.coroutines.test.runTest
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import pt.isel.api_pm.database.tables.MonitoredEndpointTable
import pt.isel.api_pm.database.tables.UserTable
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import pt.isel.api_pm.dto.metric.RequestMetric
import kotlin.test.*
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours

class MetricsRepositoryExposedTests {

    private fun createRepository(): MetricsRepositoryExposed {

        TestDatabase.init()

        transaction(TestDatabase.db) {

            UserTable.insert {
                it[id] = 1
                it[username] = "user1"
                it[passwordhash] = "hashed"
                it[createdAt] = Clock.System.now()
            }

            MonitoredEndpointTable.insert {
                it[id] = 1
                it[userId] = 1
                it[url] = "https://api.github.com"
                it[name] = "github"
                it[intervalSeconds] = 60
                it[createdAt] = Clock.System.now()

                it[notificationType] = "none"
                it[notificationData] = null

                it[alertRuleType] = null
                it[alertRuleData] = null
            }
        }

        return MetricsRepositoryExposed(
            TestDatabase.db
        )
    }

    private fun metric(
        statusCode: Int = 200,
        latency: Long = 100
    ) =
        RequestMetric(
            endpoint = EndpointUrl("https://api.github.com"),
            timestamp = Clock.System.now(),
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
                metric = metric()
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
                metric()
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

            assertEquals(
                200,
                metrics.first().statusCode
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
                metric()
            )

            repository.save(
                1u,
                1u,
                metric(statusCode = 500)
            )

            val metrics =
                repository.getAll()

            assertEquals(
                2,
                metrics.size
            )
        }

    @Test
    fun `should return metrics by interval`() =
        runTest {

            val repository =
                createRepository()

            val now =
                Clock.System.now()

            repository.save(
                1u,
                1u,
                RequestMetric(
                    endpoint = EndpointUrl("https://api.github.com"),
                    timestamp = now,
                    latency = 100,
                    statusCode = 200
                )
            )

            val metrics =
                repository.getByInterval(
                    userId = 1u,
                    monitoredEndpointId = 1u,
                    from = now.minus(1.hours),
                    to = now.plus(1.hours)
                )

            assertEquals(
                1,
                metrics.size
            )
        }
}