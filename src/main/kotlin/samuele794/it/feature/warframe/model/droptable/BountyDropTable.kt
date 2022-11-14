package samuele794.it.feature.warframe.model.droptable

import samuele794.it.feature.warframe.model.Item
import samuele794.it.feature.warframe.model.Rotation

data class BountyDropTable(
    val level: String,
    val tableRotation: TableRotation<Stage> = TableRotationImpl()
): TableRotation<Stage> by tableRotation{
    override fun addRotation(rotation: Rotation<Stage>): TableRotation<Stage> {
        return copy(
            tableRotation = tableRotation.addRotation(rotation)
        )
    }
}

data class Stage(val stageName: String, val items: List<Item> = listOf())

fun Stage.addItem(item: Item): Stage {
    val itemsUpdated = items.toMutableList().apply {
        add(item)
    }

    return copy(items = itemsUpdated)
}