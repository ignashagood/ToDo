package nktns.todo.data

import kotlinx.coroutines.flow.Flow
import nktns.todo.data.database.dao.CatalogDAO
import nktns.todo.data.database.dao.TaskDAO
import nktns.todo.data.database.entity.CatalogEntity
import nktns.todo.data.database.entity.TaskEntity
import nktns.todo.data.database.relations.CatalogWithTasks
import java.util.Date

class TaskRepository(private val taskDAO: TaskDAO, private val catalogDAO: CatalogDAO) {

    fun get(id: Int): TaskEntity? = taskDAO.get(id)

    fun getCatalog(id: Int): CatalogEntity? = catalogDAO.get(id)

    fun getTasks(): Flow<List<TaskEntity>> = taskDAO.getAll()

    fun getTodayTasks(): Flow<List<TaskEntity>> = taskDAO.getAllByCompletionDate(Date())

    suspend fun getCatalogTasks(catalogId: Int): CatalogWithTasks? = catalogDAO.getWithTasks(catalogId)

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
