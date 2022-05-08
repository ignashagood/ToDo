package nktns.todo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import nktns.todo.data.database.dao.TaskDAONew
import nktns.todo.data.database.entity.Task
import java.time.LocalDate
import java.time.ZoneId
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
        Task(
            id = FIRST_TASK_ID,
            parentId = 1,
            name = "Task 1",
            description = "Task 1 desc",
            creationDate = Date(),
            completionDate = tomorrowDate,
            isCompleted = false
        ),
        Task(
            id = 2,
            parentId = 1,
            name = "Task 2",
            description = "Task 2 desc",
            creationDate = Date(),
            completionDate = Date(),
            isCompleted = false
        )
    )
    private val tasks: MutableList<Task> = initialTasks.toMutableList()
    private val tasksFlow: MutableStateFlow<List<Task>> = MutableStateFlow(tasks)

    fun reset() {
        tasks.clear()
        tasks.addAll(initialTasks)
        runBlocking { tasksFlow.emit(tasks) }
    }

    override suspend fun add(task: Task) {
        tasks.add(task)
        tasksFlow.emit(tasks)
    }

    override suspend fun delete(task: Task) {
        if (tasks.remove(task)) {
            tasksFlow.emit(tasks)
        }
    }

    override suspend fun update(task: Task) {
        val index: Int = tasks.indexOfFirst { it.id == task.id }
        if (index >= 0) {
            tasks[index] = task
            tasksFlow.emit(tasks)
        }
    }

    override fun get(id: Int): Task? = tasks.find { it.id == id }

    override fun getTasks(): Flow<List<Task>> = tasksFlow

    override fun getTasksByCompletionDate(date: Date): Flow<List<Task>> {
        return flowOf(tasks.filter { task -> date.toLocalDate() == task.completionDate.toLocalDate() })
    }

    private fun Date.toLocalDate(): LocalDate = toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
}
