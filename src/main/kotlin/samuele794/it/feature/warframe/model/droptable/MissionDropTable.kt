package samuele794.it.feature.warframe.model.droptable

import samuele794.it.feature.warframe.model.Item
import samuele794.it.feature.warframe.model.MissionType
import samuele794.it.feature.warframe.model.Rotation

data class MissionDropTable(
    val missionName: String,
    val missionType: MissionType,
    val rotations: List<Rotation<Item>> = listOf()
){
    fun hasRotationWithException(exceptionList: List<String>): Boolean {
        return missionType.hasRotation && !exceptionList.contains(missionName)
    }
}

fun MissionDropTable.addRotation(rotation: Rotation<Item>): MissionDropTable {
    val rotationUpdated = rotations.toMutableList().apply {
        add(rotation)
    }

   return copy(rotations = rotationUpdated)
}