package pt.isel.api_pm.alert

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class AggregationType {
    @Serializable
    @SerialName("ALL")
    data object ALL : AggregationType()

    @Serializable
    @SerialName("AVG")
    data object AVG : AggregationType()

    @Serializable
    @SerialName("COUNT")
    data class COUNT(val count: Int) : AggregationType()
}