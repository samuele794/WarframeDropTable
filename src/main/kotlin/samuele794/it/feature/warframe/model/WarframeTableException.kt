package samuele794.it.feature.warframe.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WarframeTableException(
    @SerialName("missionRotationNoRotation")
    val missionRotationNoRotation: List<String>
)