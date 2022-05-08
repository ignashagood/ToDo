package nktns.todo.data

import kotlinx.coroutines.flow.Flow
import nktns.todo.data.database.dao.TaskDAONew
import nktns.todo.data.database.entity.Task
import java.util.Date

class TaskRepositoryNew(private val taskDAO: TaskDAONew) {
    suspend fun add(task: Task) {
        taskDAO.add(task)
    }

    suspend fun update(task: Task) {
        taskDAO.update(task)
    }

    suspend fun delete(task: Task) {
        taskDAO.delete(task)
    }

    fun get(taskId: Int): Task? = taskDAO.get(taskId)

    fun getTasks(): Flow<List<Task>> = taskDAO.getTasks()

    fun getTodayTasks(): Flow<List<Task>> = taskDAO.getTasksByCompletionDate(Date())
}
