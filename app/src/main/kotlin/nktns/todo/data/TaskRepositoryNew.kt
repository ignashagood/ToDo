package nktns.todo.data

import kotlinx.coroutines.flow.Flow
import nktns.todo.data.database.dao.TaskDAONew
import nktns.todo.data.database.entity.Task
import java.util.Date

class TaskRepositoryNew(private val taskDAO: TaskDAONew) {

    fun get(id: Int): Task? = taskDAO.get(id)

    fun getTasks(): Flow<List<Task>> = taskDAO.getAll()

    fun getTodayTasks(): Flow<List<Task>> = taskDAO.getAllByCompletionDate(Date())

    suspend fun add(task: Task) {
        taskDAO.add(task)
    }

    suspend fun update(task: Task) {
        taskDAO.update(task)
    }

    suspend fun delete(task: Task) {
        taskDAO.delete(task)
    }
}
