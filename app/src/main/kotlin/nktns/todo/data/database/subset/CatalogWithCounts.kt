package nktns.todo.data.database.subset

import androidx.room.Embedded
import nktns.todo.data.database.entity.CatalogEntity

data class CatalogWithCounts(
    @Embedded
    val catalog: CatalogEntity,
    val taskCount: Int,
    val outdatedTaskCount: Int
)
