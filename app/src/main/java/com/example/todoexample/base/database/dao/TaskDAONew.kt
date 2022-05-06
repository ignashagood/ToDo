package com.example.todoexample.base.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todoexample.base.database.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDAONew {

    @Insert
    suspend fun add(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Update
    suspend fun update(task: Task)

    @Query("SELECT * FROM tasks WHERE taskId = :id")
    fun get(id: Int): Task

    @Query("SELECT * FROM tasks WHERE taskCompletionDate = :todayDate ORDER BY isCompleted, creationDate")
    fun getTodayTasks(todayDate: Long): Flow<List<Task>>

    @Query("SELECT * from tasks ORDER BY isCompleted, creationDate")
    fun getTasks(): Flow<List<Task>>
}
