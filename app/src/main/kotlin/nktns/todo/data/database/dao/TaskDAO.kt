package nktns.todo.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import nktns.todo.data.database.entity.TaskEntity
import java.util.Date

@Dao
interface TaskDAO {

    @Query("SELECT * FROM tasks WHERE taskId = :id")
    fun get(id: Int): TaskEntity?

    @Query("SELECT * from tasks ORDER BY taskIsCompleted, taskCreationDate")
    fun getAll(): Flow<List<TaskEntity>>

    @Query("SELECT * from tasks WHERE taskCatalogId = :catalogId ORDER BY taskIsCompleted, taskCreationDate")
    fun getAllWithCatalogId(catalogId: Int): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE taskCompletionDate = :date ORDER BY taskIsCompleted, taskCreationDate")
    fun getAllByCompletionDate(date: Date): Flow<List<TaskEntity>>

    @Insert
    suspend fun add(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)
}
