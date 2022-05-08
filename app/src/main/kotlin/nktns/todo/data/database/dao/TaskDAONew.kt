package nktns.todo.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import nktns.todo.data.database.entity.Task
import java.util.Date

@Dao
interface TaskDAONew {

    @Insert
    suspend fun add(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Update
    suspend fun update(task: Task)

    @Query("SELECT * FROM tasks WHERE taskId = :id")
    fun get(id: Int): Task?

    @Query("SELECT * from tasks ORDER BY isCompleted, creationDate")
    fun getTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE taskCompletionDate = :date ORDER BY isCompleted, creationDate")
    fun getTasksByCompletionDate(date: Date): Flow<List<Task>>
}
