package com.example.todoexample.base.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.todoexample.base.database.entity.TASK_LIST_ID
import com.example.todoexample.base.database.entity.TASK_LIST_NAME
import com.example.todoexample.base.database.entity.TaskList
import com.example.todoexample.base.database.relations.TaskListWithTasks
import com.example.todoexample.base.database.subset.TaskListWithCounts

@Dao
interface TaskListDaoNew {
    @Insert
    fun add(taskList: TaskList)

    @Delete
    fun delete(taskList: TaskList)

    @Update
    fun update(taskList: TaskList)

    @Query("SELECT * FROM taskLists WHERE taskListId = :id")
    fun get(id: Int)

    @Query("SELECT * from taskLists ORDER BY creationDate")
    fun getTaskLists(): LiveData<List<TaskList>>

    @Transaction
    @Query("SELECT * FROM taskLists WHERE taskListName = :taskListName")
    fun getTaskListWithTasks(taskListName: String): List<TaskListWithTasks>

    @Query(
        "SELECT $TASK_LIST_ID, $TASK_LIST_NAME, " +
            "COUNT(taskId) AS taskCount," +
            "COUNT(case when taskCompletionDate < DATE('now') then taskId else null end) AS outdatedTaskCount " +
            "FROM taskLists " +
            "LEFT OUTER JOIN tasks ON taskParentId = taskListId " +
            "GROUP BY taskParentId " +
            "ORDER BY taskListId DESC"
    )
    fun get(): List<TaskListWithCounts>
}
