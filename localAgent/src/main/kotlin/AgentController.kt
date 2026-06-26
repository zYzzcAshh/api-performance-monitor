package org.api

class AgentController {
    private var monitoring = false
    private var monitoredEndpoint: AgentMonitoredEndpoint? = null

    fun isMonitoring() = monitoring

    fun setMonitoring(monitoring: Boolean) {
        this.monitoring = monitoring
    }

    fun setMonitoredEndpoint(endpoint: AgentMonitoredEndpoint) {
        this.monitoredEndpoint = endpoint
    }

    fun getMonitoredEndpoint() = monitoredEndpoint
}

data class AgentMonitoredEndpoint(
    val name: String,
    val url: String,
    val intervalSeconds: Long,
)