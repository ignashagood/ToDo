package nktns.todo.data

import kotlinx.coroutines.flow.Flow
import nktns.todo.data.database.dao.TaskDAO
import nktns.todo.data.database.entity.TaskEntity

class TaskRepository(private val taskDao: TaskDAO) {
    val sortedTasks: Flow<List<TaskEntity>> = taskDao.sort()

    suspend fun add(task: TaskEntity) {
        taskDao.add(task)
    }

    suspend fun delete(task: TaskEntity) {
        taskDao.delete(task)
    }

    suspend fun update(task: TaskEntity) {
        taskDao.update(task)
    }

    fun getById(taskId: Int): TaskEntity {
        return taskDao.getById(taskId)
    }
}
