package nktns.todo.data.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import nktns.todo.data.database.entity.CatalogEntity
import nktns.todo.data.database.entity.TaskEntity

data class CatalogWithTasks(
    @Embedded
    val catalog: CatalogEntity,
    @Relation(parentColumn = "catalogId", entityColumn = "taskCatalogId")
    val tasks: List<TaskEntity>
)
