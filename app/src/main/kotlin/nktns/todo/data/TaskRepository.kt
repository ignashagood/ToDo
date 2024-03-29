package nktns.todo.data

import kotlinx.coroutines.flow.Flow
import nktns.todo.data.database.dao.TaskDAO
import nktns.todo.data.database.entity.TaskEntity
import java.util.Date

class TaskRepository(private val taskDAO: TaskDAO) {

    fun get(id: Int): TaskEntity? = taskDAO.get(id)

    fun getTasks(): Flow<List<TaskEntity>> = taskDAO.getAll()

    fun getTodayTasks(): Flow<List<TaskEntity>> = taskDAO.getAllByCompletionDate(Date())

    fun getCatalogTasks(catalogId: Int): Flow<List<TaskEntity>> = taskDAO.getAllWithCatalogId(catalogId)

    fun getArchivedTasks(): Flow<List<TaskEntity>> = taskDAO.getArchived()

    suspend fun deleteArchived() = taskDAO.deleteArchived()

    suspend fun add(task: TaskEntity) {
        taskDAO.add(task)
    }

    suspend fun update(task: TaskEntity) {
        taskDAO.update(task)
    }

    suspend fun delete(task: TaskEntity) {
        taskDAO.delete(task)
    }
}
