package com.example.todoexample.base.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todoexample.base.database.entity.TaskEntity

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

    @Query("SELECT * FROM tasks WHERE itemId = :taskId")
    fun getById(taskId: Int): TaskEntity
}
