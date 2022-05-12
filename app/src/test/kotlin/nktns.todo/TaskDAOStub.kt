package nktns.todo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import nktns.todo.base.withoutTime
import nktns.todo.data.database.dao.TaskDAONew
import nktns.todo.data.database.entity.Task
import java.util.Calendar
import java.util.Date

class TaskDAOStub : TaskDAONew {

    companion object {
        const val INITIAL_TASKS_COUNT = 2
        const val FIRST_TASK_ID = 1
    }

    private val tomorrowDate: Date =
        Calendar.getInstance().run {
            time = Date()
            add(Calendar.DAY_OF_YEAR, 1)
            time
        }
    private val initialTasks: List<Task> = listOf(
        createTask(id = FIRST_TASK_ID),
        createTask(id = 2, completionDate = tomorrowDate),
    )
    private val tasks: MutableList<Task> = initialTasks.toMutableList()
    private val tasksFlow: MutableStateFlow<List<Task>> = MutableStateFlow(tasks.toList())

    suspend fun reset() {
        tasks.clear()
        tasks.addAll(initialTasks)
        emitTasks()
    }

    override suspend fun add(task: Task) {
        if (tasks.contains(task).not()) {
            tasks.add(task)
            tasksFlow.emit(tasks.toList())
        }
    }

    override suspend fun delete(task: Task) {
        if (tasks.remove(task)) {
            emitTasks()
        }
    }

    override suspend fun update(task: Task) {
        val index: Int = tasks.indexOfFirst { it.id == task.id }
        if (index >= 0) {
            tasks[index] = task
            emitTasks()
        }
    }

    override fun get(id: Int): Task? = tasks.find { it.id == id }

    override fun getTasks(): Flow<List<Task>> = tasksFlow

    override fun getTasksByCompletionDate(date: Date): Flow<List<Task>> {
        return flowOf(tasks.filter { task -> date.withoutTime() == task.completionDate.withoutTime() })
    }

    private suspend fun emitTasks() {
        tasksFlow.emit(tasks.toList())
    }
}
