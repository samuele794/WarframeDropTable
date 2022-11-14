package samuele794.it.feature.warframe.model.droptable

import samuele794.it.feature.warframe.model.Item

data class RelicDropTable(
    val relicName :String,
    val relicRefinements: List<RelicRefinement>
)

data class RelicRefinement(
    val relicRefinement: String,
    val relicItems: List<Item> = listOf()
)

fun RelicRefinement.addItem(item: Item): RelicRefinement {
    val itemsUpdated = relicItems.toMutableList().apply {
        add(item)
    }

    return copy(relicItems = itemsUpdated)
}