package nktns.todo.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import nktns.todo.data.database.entity.Catalog
import nktns.todo.data.database.relations.TaskListWithTasks
import nktns.todo.data.database.subset.TaskListWithCounts

@Dao
interface CatalogDAO {
    @Insert
    suspend fun add(catalog: Catalog)

    @Delete
    suspend fun delete(catalog: Catalog)

    @Update
    suspend fun update(catalog: Catalog)

    @Query("SELECT * FROM taskLists WHERE taskListId = :id")
    fun get(id: Int): Catalog

    @Query("SELECT * from taskLists ORDER BY creationDate")
    fun getTaskLists(): Flow<List<Catalog>>

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
