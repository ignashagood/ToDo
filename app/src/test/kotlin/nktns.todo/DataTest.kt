package nktns.todo

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import nktns.todo.data.TaskRepositoryNew
import nktns.todo.data.database.entity.Task
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.Calendar
import java.util.Date

class DataTest {

    private val taskDAO: TaskDAOStub = TaskDAOStub()
    private val taskRepository: TaskRepositoryNew = TaskRepositoryNew(taskDAO)

    @Test
    fun `when tasks requested there should be no exceptions`() =
        Assertions.assertDoesNotThrow { runBlocking { taskRepository.getTasks().toList() } }

    @Test
    fun `should be tasks with today's completion date when today tasks requested`() = runBlocking {
        val today = Calendar.getInstance().clearTime()

        val tasks: List<Task> = taskRepository.getTodayTasks().toList().flatten()
        val notTodayTasks: List<Task> = tasks.filterNot { it.completionDate.toCalendar().clearTime() == today }

        assertTrue(
            notTodayTasks.isEmpty(),
            "There are tasks with an unexpected date - ${notTodayTasks.map(Task::completionDate)}"
        )
    }

    //@Test
    //fun `should 1`() = runBlocking {
    //    val newTask = Task(TaskDAOStub.INITIAL_TASKS_COUNT + 1, 1, "Task 3", "Task 3 desc", Date(), Date(), false)
    //    taskDAO.add(newTask)
    //    assertTrue(taskDAO.getTasks().last().contains(newTask))
    //}

    private fun Date.toCalendar() = Calendar.getInstance().apply { time = this@toCalendar }

    private fun Calendar.clearTime() = apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}
