package nktns.todo.data.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import nktns.todo.data.database.entity.Catalog
import nktns.todo.data.database.entity.Task

data class TaskListWithTasks(
    @Embedded
    val taskList: Catalog,
    @Relation(parentColumn = "taskListId", entityColumn = "taskParentId")
    val tasks: List<Task>
)
