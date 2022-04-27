package com.example.todoexample.database.dao

import androidx.lifecycle.LiveData
import com.example.todoexample.database.entity.TaskEntity
import androidx.room.*


@Dao
interface TaskDAO {

    @Query("SELECT * FROM tasks")
    fun getAll(): LiveData<List<TaskEntity>>

    @Insert
    suspend fun add(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)

    @Query("SELECT * from tasks ORDER BY isCompleted")
    fun sort(): LiveData<List<TaskEntity>>
}