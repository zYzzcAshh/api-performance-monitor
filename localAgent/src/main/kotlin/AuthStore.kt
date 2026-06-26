package org.api

class AuthStore() {
    private var token: String? = null
    private var agentToken: String? = null

    fun setToken(newToken: String) {
        println("User token is $newToken")
        token = newToken
    }

    fun getToken(): String? {
        return token
    }

    fun clearToken() {
        token = null
    }

    fun setAgentToken(newAgentToken: String) {
        println("Agent token is $newAgentToken")
        agentToken = newAgentToken
    }

    fun getAgentToken(): String? {
        return agentToken
    }

    fun clearAgentToken() {
        agentToken = null
    }
}