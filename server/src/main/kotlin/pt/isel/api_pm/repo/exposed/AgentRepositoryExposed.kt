package pt.isel.api_pm.repo.exposed

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import pt.isel.api_pm.database.tables.AgentTable
import pt.isel.api_pm.domain.agent.Agent
import pt.isel.api_pm.domain.agent.AgentEndpoint
import pt.isel.api_pm.domain.endpoint.IntervalSeconds
import pt.isel.api_pm.repo.AgentRepository
import kotlin.time.Clock

class AgentRepositoryExposed(
    private val db: Database
) : AgentRepository {

    override suspend fun register(
        userId: UInt,
        name: String,
        token: String
    ): Agent {

        val createdAt =
            Clock.System.now()

        val id =
            transaction(db) {

                AgentTable.insert {

                    it[AgentTable.userId] = userId.toInt()
                    it[AgentTable.name] = name
                    it[AgentTable.token] = token
                    it[AgentTable.createdAt] = createdAt
                }[AgentTable.id]
            }

        return Agent(
            id = id.toUInt(),
            userId = userId,
            name = name,
            token = token,
            createdAt = createdAt,
            endpoint = null
        )
    }

    override suspend fun addEndpoint(
        userId: UInt,
        agentId: UInt,
        name: String,
        intervalSeconds: IntervalSeconds
    ) {

        transaction(db) {

            val existing =
                AgentTable
                    .selectAll()
                    .where {
                        (AgentTable.id eq agentId.toInt()) and
                                (AgentTable.userId eq userId.toInt())
                    }
                    .singleOrNull()
                    ?: throw IllegalArgumentException(
                        "Agent with id $agentId not found"
                    )

            if (existing[AgentTable.endpointName] != null) {

                throw IllegalStateException(
                    "Agent with id $agentId already has an endpoint"
                )
            }

            AgentTable.update({
                (AgentTable.id eq agentId.toInt()) and
                        (AgentTable.userId eq userId.toInt())
            }) {

                it[endpointName] = name
                it[endpointIntervalSeconds] = intervalSeconds.value
                it[endpointCreatedAt] = Clock.System.now()
            }
        }
    }

    override suspend fun getById(
        userId: UInt,
        agentId: UInt
    ): Agent? =
        transaction(db) {

            AgentTable
                .selectAll()
                .where {
                    (AgentTable.id eq agentId.toInt()) and
                            (AgentTable.userId eq userId.toInt())
                }
                .singleOrNull()
                ?.toAgent()
        }

    private fun ResultRow.toAgent(): Agent {

        val endpointName =
            this[AgentTable.endpointName]

        val endpointInterval =
            this[AgentTable.endpointIntervalSeconds]

        val endpointCreatedAt =
            this[AgentTable.endpointCreatedAt]

        val endpoint =
            if (
                endpointName != null &&
                endpointInterval != null &&
                endpointCreatedAt != null
            ) {

                AgentEndpoint(
                    name = endpointName,
                    intervalSeconds = IntervalSeconds(
                        endpointInterval
                    ),
                    createdAt = endpointCreatedAt
                )

            } else {
                null
            }

        return Agent(
            id = this[AgentTable.id].toUInt(),
            userId = this[AgentTable.userId].toUInt(),
            name = this[AgentTable.name],
            token = this[AgentTable.token],
            createdAt = this[AgentTable.createdAt],
            endpoint = endpoint
        )
    }
}