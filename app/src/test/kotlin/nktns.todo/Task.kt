package nktns.todo

import nktns.todo.data.database.entity.Task
import java.util.Date

fun createTask(id: Int, completionDate: Date = Date()) =
    Task(
        id = id,
        parentId = 1,
        name = "Task $id",
        description = "Task $id desc",
        creationDate = Date(),
        completionDate = completionDate,
        isCompleted = false
    )
