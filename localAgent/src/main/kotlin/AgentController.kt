package org.api

import pt.isel.api_pm.domain.endpoint.HttpMethod
import java.io.File

class AgentController(
    val propertiesFileStore: PropertiesFileStore,
    private val storeFile: File = File(
        File(System.getProperty("user.dir"), "data"),
        "auth.properties"
    )
) {
    private var monitoring = false
    private var monitoredEndpoint: AgentMonitoredEndpoint? = null

    init {
        load()
    }

    fun isMonitoring() = monitoring

    fun setMonitoring(monitoring: Boolean) {
        this.monitoring = monitoring
        save()
    }

    fun setMonitoredEndpoint(endpoint: AgentMonitoredEndpoint) {
        this.monitoredEndpoint = endpoint
        save()
    }

    fun clear() {
        this.monitoredEndpoint = null
        this.monitoring = false
        save()
    }

    fun getMonitoredEndpoint() = monitoredEndpoint

    private fun load() {
        val props = propertiesFileStore.read(storeFile)

        monitoring = props.getProperty(KEY_MONITORING)?.toBoolean() ?: false

        val name = props.getProperty(KEY_ENDPOINT_NAME)
        val methodName = props.getProperty(KEY_ENDPOINT_METHOD)
        val url = props.getProperty(KEY_ENDPOINT_URL)
        val interval = props.getProperty(KEY_ENDPOINT_INTERVAL)?.toLongOrNull()

        monitoredEndpoint = if (name != null && methodName != null && url != null && interval != null) {
            try {
                AgentMonitoredEndpoint(
                    name = name,
                    method = HttpMethod.valueOf(methodName),
                    url = url,
                    intervalSeconds = interval
                )
            } catch (_: Exception) {
                null
            }
        } else {
            null
        }
    }

    private fun save() {
        propertiesFileStore.update(storeFile) { props ->
            props.setProperty(KEY_MONITORING, monitoring.toString())

            val endpoint = monitoredEndpoint
            if (endpoint != null) {
                props.setProperty(KEY_ENDPOINT_NAME, endpoint.name)
                props.setProperty(KEY_ENDPOINT_METHOD, endpoint.method.name)
                props.setProperty(KEY_ENDPOINT_URL, endpoint.url)
                props.setProperty(KEY_ENDPOINT_INTERVAL, endpoint.intervalSeconds.toString())
            } else {
                props.remove(KEY_ENDPOINT_NAME)
                props.remove(KEY_ENDPOINT_METHOD)
                props.remove(KEY_ENDPOINT_URL)
                props.remove(KEY_ENDPOINT_INTERVAL)
            }
        }
    }

    companion object {
        private const val KEY_MONITORING = "agent.monitoring"
        private const val KEY_ENDPOINT_NAME = "agent.endpoint.name"
        private const val KEY_ENDPOINT_METHOD = "agent.endpoint.method"
        private const val KEY_ENDPOINT_URL = "agent.endpoint.url"
        private const val KEY_ENDPOINT_INTERVAL = "agent.endpoint.intervalSeconds"
    }
}

data class AgentMonitoredEndpoint(
    val name: String,
    val method: HttpMethod,
    val url: String,
    val intervalSeconds: Long,
)