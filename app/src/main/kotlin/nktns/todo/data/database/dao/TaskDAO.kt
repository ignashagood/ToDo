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

/** Дата - 13 цифр, деление на 1000 происходит для избавления от микросекунд, а модификатор unixepoch
интерпретирует дату как время Unix - количество секунд с 1970 года **/
    @Query(
        """
            SELECT * FROM tasks 
            WHERE date(taskCompletionDate / 1000) = date(:date / 1000, 'unixepoch') 
            ORDER BY taskIsCompleted, taskCreationDate
            """
    )
    fun getAllByCompletionDate(date: Date): Flow<List<TaskEntity>>

    @Insert
    suspend fun add(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)
}
