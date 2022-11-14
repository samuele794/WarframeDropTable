package samuele794.it.feature.warframe.model

import samuele794.it.feature.warframe.model.droptable.Stage

data class Rotation<T>(
    val name: String,
    val items: List<T> = listOf()
)

fun <T> Rotation<T>.addItem(item: T): Rotation<T> {
    val itemsUpdated = items.toMutableList().apply {
        add(item)
    }

    return copy(
        items = itemsUpdated
    )
}