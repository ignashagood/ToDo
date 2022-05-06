package com.example.todoexample.base.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.todoexample.base.database.entity.Task
import com.example.todoexample.base.database.entity.TaskList

data class TaskListWithTasks(
    @Embedded
    val taskList: TaskList,
    @Relation(parentColumn = "taskListId", entityColumn = "taskParentId")
    val tasks: List<Task>
)
