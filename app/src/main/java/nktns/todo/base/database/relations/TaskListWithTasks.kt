package nktns.todo.base.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import nktns.todo.base.database.entity.Task
import nktns.todo.base.database.entity.TaskList

data class TaskListWithTasks(
    @Embedded
    val taskList: TaskList,
    @Relation(parentColumn = "taskListId", entityColumn = "taskParentId")
    val tasks: List<Task>
)
