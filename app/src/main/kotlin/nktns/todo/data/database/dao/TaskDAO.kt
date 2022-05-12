package nktns.todo.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import nktns.todo.data.database.entity.TaskEntity

@Dao
interface TaskDAO {
    @Insert
    suspend fun add(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)

    @Query("SELECT * from taskItems ORDER BY isCompleted")
    fun sort(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM taskItems WHERE itemId = :taskId")
    fun getById(taskId: Int): TaskEntity
}
