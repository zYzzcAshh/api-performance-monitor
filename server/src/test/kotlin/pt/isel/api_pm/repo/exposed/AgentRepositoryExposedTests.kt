package pt.isel.api_pm.repo.exposed

import kotlinx.coroutines.test.runTest
import kotlin.test.*
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import pt.isel.api_pm.database.tables.UserTable
import pt.isel.api_pm.domain.endpoint.IntervalSeconds

import kotlin.time.Clock

class AgentRepositoryExposedTests {

    private fun createRepository(): AgentRepositoryExposed {

        TestDatabase.init()

        return AgentRepositoryExposed(
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
    fun `should register agent`() =
        runTest {

            val repository =
                createRepository()

            createTestUser(1)

            val agent =
                repository.register(
                    userId = 1u,
                    name = "agent-1",
                    token = "token-123"
                )

            assertEquals(
                1u,
                agent.userId
            )

            assertEquals(
                "agent-1",
                agent.name
            )

            assertEquals(
                "token-123",
                agent.token
            )

            assertNull(
                agent.endpoint
            )
        }

    @Test
    fun `should get agent by id`() =
        runTest {

            val repository =
                createRepository()

            createTestUser(1)

            val created =
                repository.register(
                    userId = 1u,
                    name = "agent-1",
                    token = "token-123"
                )

            val agent =
                repository.getById(
                    userId = 1u,
                    agentId = created.id
                )

            assertNotNull(agent)

            assertEquals(
                created.id,
                agent.id
            )

            assertEquals(
                "agent-1",
                agent.name
            )
        }

    @Test
    fun `should add endpoint to agent`() =
        runTest {

            val repository =
                createRepository()

            createTestUser(1)

            val agent =
                repository.register(
                    userId = 1u,
                    name = "agent-1",
                    token = "token-123"
                )

            repository.addEndpoint(
                userId = 1u,
                agentId = agent.id,
                name = "health-check",
                intervalSeconds = IntervalSeconds(60)
            )

            val updated =
                repository.getById(
                    userId = 1u,
                    agentId = agent.id
                )

            assertNotNull(updated)

            val endpoint =
                updated.endpoint

            assertNotNull(endpoint)

            assertEquals(
                "health-check",
                endpoint.name
            )

            assertEquals(
                60,
                endpoint.intervalSeconds.value
            )
        }

    @Test
    fun `should return null for missing agent`() =
        runTest {

            val repository =
                createRepository()

            createTestUser(1)

            val agent =
                repository.getById(
                    userId = 1u,
                    agentId = 999u
                )

            assertNull(agent)
        }

    @Test
    fun `should fail when adding endpoint twice`() =
        runTest {

            val repository =
                createRepository()

            createTestUser(1)

            val agent =
                repository.register(
                    userId = 1u,
                    name = "agent-1",
                    token = "token-123"
                )

            repository.addEndpoint(
                userId = 1u,
                agentId = agent.id,
                name = "endpoint-1",
                intervalSeconds = IntervalSeconds(60)
            )

            assertFailsWith<IllegalStateException> {

                repository.addEndpoint(
                    userId = 1u,
                    agentId = agent.id,
                    name = "endpoint-2",
                    intervalSeconds = IntervalSeconds(120)
                )
            }
        }
}