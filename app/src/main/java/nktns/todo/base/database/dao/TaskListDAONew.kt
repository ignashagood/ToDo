package nktns.todo.base.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import nktns.todo.base.database.entity.TaskList
import nktns.todo.base.database.relations.TaskListWithTasks
import nktns.todo.base.database.subset.TaskListWithCounts

@Dao
interface TaskListDAONew {
    @Insert
    suspend fun add(taskList: TaskList)

    @Delete
    suspend fun delete(taskList: TaskList)

    @Update
    suspend fun update(taskList: TaskList)

    @Query("SELECT * FROM taskLists WHERE taskListId = :id")
    fun get(id: Int): TaskList

    @Query("SELECT * from taskLists ORDER BY creationDate")
    fun getTaskLists(): Flow<List<TaskList>>

    @Transaction
    @Query("SELECT * FROM taskLists WHERE taskListId = :id")
    fun getTaskListWithTasks(id: Int): TaskListWithTasks

    @Query(
        "SELECT taskListId, taskListName, " +
                "COUNT(taskId) AS taskCount," +
                "COUNT(case when taskCompletionDate < DATE('now') then taskId else null end) AS outdatedTaskCount " +
                "FROM taskLists " +
                "LEFT OUTER JOIN tasks ON taskParentId = taskListId " +
                "GROUP BY taskParentId " +
                "ORDER BY taskListId DESC"
    )
    fun getTaskListWithCounts(): List<TaskListWithCounts>
}
