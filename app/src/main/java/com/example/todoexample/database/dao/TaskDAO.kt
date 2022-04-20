package com.example.todoexample.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.todoexample.Task
import com.example.todoexample.database.entity.TaskEntity

@Dao
interface TaskDAO {

    @Query("SELECT * FROM tasks")
    fun getAll(): LiveData<List<TaskEntity>>

    @Insert
    suspend fun add(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)
}