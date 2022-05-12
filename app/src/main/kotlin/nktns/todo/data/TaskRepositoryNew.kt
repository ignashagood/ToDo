package nktns.todo.data

import kotlinx.coroutines.flow.Flow
import nktns.todo.data.database.dao.TaskDAONew
import nktns.todo.data.database.entity.TaskEntityNew
import java.util.Date

class TaskRepositoryNew(private val taskDAO: TaskDAONew) {

    fun get(id: Int): TaskEntityNew? = taskDAO.get(id)

    fun getTasks(): Flow<List<TaskEntityNew>> = taskDAO.getAll()

    fun getTodayTasks(): Flow<List<TaskEntityNew>> = taskDAO.getAllByCompletionDate(Date())

    suspend fun add(task: TaskEntityNew) {
        taskDAO.add(task)
    }

    suspend fun update(task: TaskEntityNew) {
        taskDAO.update(task)
    }

    suspend fun delete(task: TaskEntityNew) {
        taskDAO.delete(task)
    }
}
