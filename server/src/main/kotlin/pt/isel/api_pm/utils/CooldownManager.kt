package pt.isel.api_pm.utils

import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.minutes

class CooldownManager(
    private val cooldownMs : Long = 15.minutes.inWholeMilliseconds,
) {
    private enum class Kind { ENDPOINT, AGENT }

    private data class CooldownKey(
        val kind: Kind,
        val userId: UInt,
        val entityId: UInt
    )

    private val cooldowns = ConcurrentHashMap<CooldownKey, Long>()

    fun markCooldown(userId: UInt, endpointId: UInt) {
        mark(Kind.ENDPOINT, userId, endpointId)
    }

    fun markAgentCooldown(userId: UInt, agentId: UInt) {
        mark(Kind.AGENT, userId, agentId)
    }

    fun isInCooldown(userId: UInt, endpointId: UInt): Boolean {
        return isInCooldown(Kind.ENDPOINT, userId, endpointId)
    }

    fun isInCooldownAgent(userId: UInt, agentId: UInt): Boolean {
        return isInCooldown(Kind.AGENT, userId, agentId)
    }

    private fun mark(kind: Kind, userId: UInt, entityId: UInt) {
        cooldowns[CooldownKey(kind, userId, entityId)] = System.currentTimeMillis()
    }

    private fun isInCooldown(kind: Kind, userId: UInt, entityId: UInt): Boolean {
        val cooldownStart = cooldowns[CooldownKey(kind, userId, entityId)] ?: return false
        val now = System.currentTimeMillis()
        return (now - cooldownStart) < cooldownMs
    }
}