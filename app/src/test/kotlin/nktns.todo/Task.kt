package nktns.todo

import nktns.todo.data.database.entity.TaskEntityNew
import java.util.Date

fun createTask(id: Int, completionDate: Date = Date()) =
    TaskEntityNew(
        id = id,
        name = "Task $id",
        description = "Task $id desc",
        creationDate = Date(),
        completionDate = completionDate,
        isCompleted = false,
        catalogId = 1,
    )
