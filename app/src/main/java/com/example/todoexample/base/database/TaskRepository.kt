package com.example.todoexample.base.database

import androidx.lifecycle.LiveData
import com.example.todoexample.base.database.dao.TaskDAO
import com.example.todoexample.base.database.entity.TaskEntity

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

    suspend fun getById(taskId: Int): TaskEntity {
        return taskDao.getById(taskId)
    }
}
