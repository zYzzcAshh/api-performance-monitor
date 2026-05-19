package pt.isel.api_pm.repo.exposed

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import pt.isel.api_pm.database.tables.UserTable
import pt.isel.api_pm.domain.user.PasswordHash
import pt.isel.api_pm.domain.user.User
import pt.isel.api_pm.domain.user.Username
import pt.isel.api_pm.repo.UserRepository
import kotlin.time.Clock

class UserRepositoryExposed(
    private val db: Database
) : UserRepository {

    override suspend fun getUsers(): List<User> =
        transaction(db) {

            UserTable.selectAll().map { row ->
                row.toUser()
            }
        }

    override suspend fun getUserById(id: UInt): User? =
        transaction(db) {

            UserTable
                .selectAll()
                .where { UserTable.id eq id.toInt() }
                .map { it.toUser() }
                .singleOrNull()
        }

    override suspend fun getUserByUsername(username: Username): User? =
        transaction(db) {

            UserTable
                .selectAll()
                .where { UserTable.username eq username.value }
                .map { it.toUser() }
                .singleOrNull()
        }

    override suspend fun addUser(user: User) {

        transaction(db) {

            UserTable.insert {
                it[username] = user.username.value
                it[passwordhash] = user.passwordHash.value
                it[createdAt] = user.createdAt
            }
        }
    }

    override suspend fun registerUser(
        username: Username,
        passwordHash: PasswordHash,
    ): User {
        val createdAt = Clock.System.now()

        val generatedId = transaction(db) {
            UserTable.insert{
                it[UserTable.username] = username.value
                it[UserTable.passwordhash] = passwordHash.value
                it[UserTable.createdAt] = createdAt
            }[UserTable.id]
        }

        return User(
            id = generatedId.toUInt(),
            username = username,
            passwordHash = passwordHash,
            createdAt = createdAt
        )
    }

    private fun ResultRow.toUser(): User =
        User(
            id = this[UserTable.id].toUInt(),
            username = Username(this[UserTable.username]),
            passwordHash = PasswordHash(this[UserTable.passwordhash]),
            createdAt = this[UserTable.createdAt]
        )
}