package nktns.todo.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import nktns.todo.data.database.entity.TaskEntityNew
import java.util.Date

@Dao
interface TaskDAONew {

    @Query("SELECT * FROM tasks WHERE taskId = :id")
    fun get(id: Int): TaskEntityNew?

    @Query("SELECT * from tasks ORDER BY taskIsCompleted, taskCreationDate")
    fun getAll(): Flow<List<TaskEntityNew>>

    @Query("SELECT * FROM tasks WHERE taskCompletionDate = :date ORDER BY taskIsCompleted, taskCreationDate")
    fun getAllByCompletionDate(date: Date): Flow<List<TaskEntityNew>>

    @Insert
    suspend fun add(task: TaskEntityNew)

    @Delete
    suspend fun delete(task: TaskEntityNew)

    @Update
    suspend fun update(task: TaskEntityNew)
}
