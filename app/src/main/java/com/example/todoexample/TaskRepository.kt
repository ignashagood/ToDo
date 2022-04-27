package com.example.todoexample

import androidx.lifecycle.LiveData
import com.example.todoexample.database.dao.TaskDAO
import com.example.todoexample.database.entity.TaskEntity

class TaskRepository(private val taskDao: TaskDAO) {
    val allTasks: LiveData<List<TaskEntity>> = taskDao.getAll()
    val sortedTasks: LiveData<List<TaskEntity>> = taskDao.sort()

    suspend fun add(task: TaskEntity) {
        taskDao.add(task)
    }

    suspend fun delete(task: TaskEntity) {
        taskDao.delete(task)
    }

    suspend fun update(task: TaskEntity) {
        taskDao.update(task)
    }
}