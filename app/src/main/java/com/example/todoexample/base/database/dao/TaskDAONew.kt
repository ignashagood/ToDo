package com.example.todoexample.base.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.todoexample.base.database.entity.Task
import com.example.todoexample.base.database.entity.TaskList
import com.example.todoexample.base.database.relations.TaskListWithTasks

data class TaskListWithCounts(val id: Int, val taskListName: String, val taskCount: Int, val outdatedTaskCount: Int)

@Dao
interface TaskDAONew {

    @Insert
    suspend fun addTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM tasks WHERE taskId = :taskId")
    fun getById(taskId: Int): Task

    @Query("SELECT * FROM tasks WHERE taskCompletionDate = :todayDate ORDER BY isCompleted")
    fun getTasksByTodayDate(todayDate: Long): LiveData<List<Task>>

    @Query("SELECT * from tasks ORDER BY isCompleted, creationDate")
    fun getSortedTasksByCreateDate(): LiveData<List<Task>>

    @Query("SELECT * from taskLists ORDER BY creationDate")
    fun getAllTaskListsByCreateDate(): LiveData<List<TaskList>>

    @Transaction
    @Query("SELECT * FROM taskLists WHERE taskListName = :taskListName")
    fun getTaskListWithTasks(taskListName: String): List<TaskListWithTasks>

    @Query(
        "SELECT taskListId, taskListName, " +
            "COUNT(taskId) AS taskCount," +
            "COUNT(case when taskCompletionDate < DATE('now') then taskId else null end) AS outdatedTaskCount " +
            "FROM taskLists " +
            "LEFT OUTER JOIN tasks ON taskParentId = taskListId " +
            "GROUP BY taskParentId " +
            "ORDER BY taskListId DESC"
    )
    fun get(): List<TaskListWithCounts>
}
