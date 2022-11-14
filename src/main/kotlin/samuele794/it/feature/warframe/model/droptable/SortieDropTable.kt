package samuele794.it.feature.warframe.model.droptable

import samuele794.it.feature.warframe.model.Item
import samuele794.it.feature.warframe.model.Rotation

data class SortieDropTable(
    val locationName : String,
    val tableRotation: TableRotation<Item> = TableRotationImpl()
): TableRotation<Item> by tableRotation{
    override fun addRotation(rotation: Rotation<Item>): TableRotation<Item> {
        return copy(
            tableRotation = tableRotation.addRotation(rotation)
        )
    }
}