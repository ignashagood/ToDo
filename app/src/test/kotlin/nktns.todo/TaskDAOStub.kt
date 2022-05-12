package nktns.todo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import nktns.todo.base.withoutTime
import nktns.todo.data.database.dao.TaskDAONew
import nktns.todo.data.database.entity.TaskEntityNew
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
    private val initialTasks: List<TaskEntityNew> = listOf(
        createTask(id = FIRST_TASK_ID),
        createTask(id = 2, completionDate = tomorrowDate),
    )
    private val tasks: MutableList<TaskEntityNew> = initialTasks.toMutableList()
    private val tasksFlow: MutableStateFlow<List<TaskEntityNew>> = MutableStateFlow(tasks.toList())

    suspend fun reset() {
        tasks.clear()
        tasks.addAll(initialTasks)
        emitTasks()
    }

    override fun get(id: Int): TaskEntityNew? = tasks.find { it.id == id }

    override fun getAll(): Flow<List<TaskEntityNew>> = tasksFlow

    override fun getAllByCompletionDate(date: Date): Flow<List<TaskEntityNew>> {
        return flowOf(tasks.filter { task -> date.withoutTime() == task.completionDate.withoutTime() })
    }

    override suspend fun add(task: TaskEntityNew) {
        if (tasks.contains(task).not()) {
            tasks.add(task)
            tasksFlow.emit(tasks.toList())
        }
    }

    override suspend fun delete(task: TaskEntityNew) {
        if (tasks.remove(task)) {
            emitTasks()
        }
    }

    override suspend fun update(task: TaskEntityNew) {
        val index: Int = tasks.indexOfFirst { it.id == task.id }
        if (index >= 0) {
            tasks[index] = task
            emitTasks()
        }
    }

    private suspend fun emitTasks() {
        tasksFlow.emit(tasks.toList())
    }
}
