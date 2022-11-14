package samuele794.it.feature.warframe.model.droptable

import samuele794.it.feature.warframe.model.Rotation

interface TableRotation<T> {
    val rotations: List<Rotation<T>>

    fun addRotation(rotation: Rotation<T>): TableRotation<T>
}

data class TableRotationImpl<T>(override val rotations: List<Rotation<T>> = listOf()) : TableRotation<T> {
    override fun addRotation(rotation: Rotation<T>): TableRotation<T> {
        val rotationUpdated = rotations.toMutableList().apply {
            add(rotation)
        }

        return copy(
            rotations = rotationUpdated
        )
    }

}