package pt.isel.api_pm.database.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestamp

object UserTable: Table("users") {
    val id = integer("id").autoIncrement()
    val username = varchar("name", 255)
    val passwordhash = varchar("passwordhash", 255)
    val createdAt = timestamp("created_at")

    override val primaryKey = PrimaryKey(id)
}