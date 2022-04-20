package com.example.todoexample

import androidx.lifecycle.LiveData
import com.example.todoexample.database.dao.TaskDAO
import com.example.todoexample.database.entity.TaskEntity

class TaskRepository(private val taskDao: TaskDAO) {
    val allTasks: LiveData<List<TaskEntity>> = taskDao.getAll()

    suspend fun add(task: TaskEntity) {
        taskDao.add(task)
    }
}