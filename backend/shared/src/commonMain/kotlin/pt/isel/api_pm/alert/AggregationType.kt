package pt.isel.api_pm.alert

import kotlinx.serialization.Serializable

@Serializable
sealed class AggregationType {
    @Serializable
    data object ALL : AggregationType()

    @Serializable
    data object AVG : AggregationType()

    @Serializable
    data class COUNT(val count: Int) : AggregationType()
}