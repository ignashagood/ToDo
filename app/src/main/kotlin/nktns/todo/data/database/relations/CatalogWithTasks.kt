package nktns.todo.data.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import nktns.todo.data.database.entity.Catalog
import nktns.todo.data.database.entity.Task

data class CatalogWithTasks(
    @Embedded
    val catalog: Catalog,
    @Relation(parentColumn = "catalogId", entityColumn = "taskParentId")
    val tasks: List<Task>
)
