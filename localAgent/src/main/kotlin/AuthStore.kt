package org.api

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

class AuthStore(
    val propertiesFileStore: PropertiesFileStore,
    private val storeFile: File = File(
        File(System.getProperty("user.dir"), "data"),
        "auth.properties"
    )
) {
    private var token: String? = null
    private var agentToken: String? = null

    init {
        load()
    }

    fun setToken(newToken: String) {
        token = newToken
        save()
    }

    fun getToken(): String? {
        return token
    }

    fun clear() {
        token = null
        agentToken = null
        save()
    }

    fun setAgentToken(newAgentToken: String) {
        agentToken = newAgentToken
        save()
    }

    fun getAgentToken(): String? {
        return agentToken
    }

    private fun load() {
        val props = propertiesFileStore.read(storeFile)
        token = props.getProperty(KEY_TOKEN)?.takeIf { it.isNotBlank() }
        agentToken = props.getProperty(KEY_AGENT_TOKEN)?.takeIf { it.isNotBlank() }
    }

    private fun save() {
        propertiesFileStore.update(storeFile) { props ->
            if (token != null) props.setProperty(KEY_TOKEN, token) else props.remove(KEY_TOKEN)
            if (agentToken != null) props.setProperty(KEY_AGENT_TOKEN, agentToken) else props.remove(KEY_AGENT_TOKEN)
        }
    }

    companion object {
        private const val KEY_TOKEN = "token"
        private const val KEY_AGENT_TOKEN = "agentToken"
    }
}