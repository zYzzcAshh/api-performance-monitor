package pt.isel.api_pm.repo.memory

import kotlinx.coroutines.test.runTest
import pt.isel.api_pm.domain.endpoint.IntervalSeconds
import kotlin.test.*

class AgentRepositoryMemoryTests {

    private fun createRepository() =
        AgentRepositoryMemory()

    @Test
    fun `should register agent`() =
        runTest {

            val repository =
                createRepository()

            val agent =
                repository.register(
                    userId = 1u,
                    name = "agent-1",
                    token = "token-123"
                )

            assertEquals(
                0u,
                agent.id
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
    fun `should generate incremental ids`() =
        runTest {

            val repository =
                createRepository()

            val first =
                repository.register(
                    1u,
                    "agent-1",
                    "token-1"
                )

            val second =
                repository.register(
                    1u,
                    "agent-2",
                    "token-2"
                )

            assertEquals(
                0u,
                first.id
            )

            assertEquals(
                1u,
                second.id
            )
        }

    @Test
    fun `should add endpoint to agent`() =
        runTest {

            val repository =
                createRepository()

            val agent =
                repository.register(
                    1u,
                    "agent-1",
                    "token-1"
                )

            repository.addEndpoint(
                userId = 1u,
                agentId = agent.id,
                name = "local-api",
                intervalSeconds = IntervalSeconds(60)
            )

            val updatedAgent =
                repository.getById(
                    1u,
                    agent.id
                )

            assertNotNull(updatedAgent)

            assertNotNull(
                updatedAgent.endpoint
            )

            assertEquals(
                "local-api",
                updatedAgent.endpoint?.name
            )
        }

    @Test
    fun `should reject adding endpoint to missing user`() =
        runTest {

            val repository =
                createRepository()

            assertFailsWith<IllegalArgumentException> {

                repository.addEndpoint(
                    userId = 999u,
                    agentId = 0u,
                    name = "local-api",
                    intervalSeconds = IntervalSeconds(60)
                )
            }
        }

    @Test
    fun `should reject adding endpoint to missing agent`() =
        runTest {

            val repository =
                createRepository()

            repository.register(
                1u,
                "agent-1",
                "token-1"
            )

            assertFailsWith<IllegalArgumentException> {

                repository.addEndpoint(
                    userId = 1u,
                    agentId = 999u,
                    name = "local-api",
                    intervalSeconds = IntervalSeconds(60)
                )
            }
        }

    @Test
    fun `should reject adding second endpoint`() =
        runTest {

            val repository =
                createRepository()

            val agent =
                repository.register(
                    1u,
                    "agent-1",
                    "token-1"
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
                    intervalSeconds = IntervalSeconds(60)
                )
            }
        }
}