package nktns.todo.data.database.subset

import androidx.room.Embedded
import nktns.todo.data.database.entity.Catalog

data class CatalogWithCounts(
    @Embedded
    val catalog: Catalog,
    val taskCount: Int,
    val outdatedTaskCount: Int
)
