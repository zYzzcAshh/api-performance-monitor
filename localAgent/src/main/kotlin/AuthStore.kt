package org.api

class AuthStore() {
    private var token: String? = null
    private var agentToken: String? = null

    fun setToken(newToken: String) {
        token = newToken
    }

    fun getToken(): String? {
        return token
    }

    fun clearToken() {
        token = null
    }

    fun setAgentToken(newAgentToken: String) {
        agentToken = newAgentToken
    }

    fun getAgentToken(): String? {
        return agentToken
    }

    fun clearAgentToken() {
        agentToken = null
    }
}