package nktns.todo

import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import nktns.todo.base.withoutTime
import nktns.todo.data.TaskRepositoryNew
import nktns.todo.data.database.entity.Task
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.util.Date

class DataTest {

    private val taskDAO: TaskDAOStub = TaskDAOStub()
    private val taskRepository: TaskRepositoryNew = TaskRepositoryNew(taskDAO)

    @BeforeEach
    fun resetDAO() {
        runBlocking { taskDAO.reset() }
    }

    //TODO Лишний тест
    @Test
    fun `when tasks requested there must be no exceptions`() =
        Assertions.assertDoesNotThrow { runBlocking { getLastTaskList() } }

    @Test
    fun `when today tasks requested, list of tasks with today's completion date must be emitted`() =
        runBlocking {
            val today = Date().withoutTime()

            val notTodayTasks: List<Task> =
                getLastTodayTaskList().filterNot { it.completionDate.withoutTime() == today }

            assertTrue(
                notTodayTasks.isEmpty(),
                "There are tasks with an unexpected date - ${notTodayTasks.map(Task::completionDate)}"
            )
        }

    @Test
    fun `when task was added, list of tasks must be emitted with it`() =
        runBlocking {
            val newTask = createTask(id = TaskDAOStub.INITIAL_TASKS_COUNT + 1)

            taskRepository.add(newTask)

            assertTrue(getLastTaskList().contains(newTask))
        }

    //TODO Лишний тест
    @Test
    fun `when task was added twice, list of tasks must be emitted with it in one instance`() =
        runBlocking {
            val newTask = createTask(id = TaskDAOStub.INITIAL_TASKS_COUNT + 1)

            taskRepository.add(newTask)
            taskRepository.add(newTask)

            assertEquals(getLastTaskList().count { it.id == newTask.id }, 1)
        }

    @Test
    fun `when delete exist task, list of tasks must be emit containing all previous tasks except the deleted one`() =
        runBlocking {
            val preDeleteTasks: List<Task> = getLastTaskList()

            taskRepository.delete(preDeleteTasks.first())
            val postDeleteTasks: List<Task> = getLastTaskList()

            assertAll(
                { assertFalse(postDeleteTasks.contains(preDeleteTasks.first())) },
                { assertTrue(postDeleteTasks.containsAll(preDeleteTasks.subList(1, preDeleteTasks.size))) },
            )
        }

    @Test
    fun `when delete non-existent task, list of tasks must be same`() =
        runBlocking {
            val preDeleteTasks: List<Task> = getLastTaskList()
            val taskForDelete = createTask(id = TaskDAOStub.INITIAL_TASKS_COUNT + 1)

            taskRepository.delete(taskForDelete)

            assertSame(preDeleteTasks, getLastTaskList())
        }

    @Test
    fun `when update exist task, list of tasks must be emit containing it with changes`() =
        runBlocking {
            val preUpdateTasks: List<Task> = getLastTaskList()
            val taskForUpdate = preUpdateTasks.first().copy(isCompleted = true)

            taskRepository.update(taskForUpdate)
            val postUpdateTasks: List<Task> = getLastTaskList()
            val updatedTask: Task? = postUpdateTasks.find { it.id == taskForUpdate.id }

            assertEquals(updatedTask, taskForUpdate)
        }

    @Test
    fun `when update non-existent task, list of tasks must be unchanged`() =
        runBlocking {
            val preUpdateTasks: List<Task> = getLastTaskList()
            val taskForUpdate = createTask(id = TaskDAOStub.INITIAL_TASKS_COUNT + 1)

            taskRepository.update(taskForUpdate)

            assertTrue(preUpdateTasks === getLastTaskList())
        }

    @Test
    fun `when get exist task by id, must be return it`() =
        runBlocking {
            val task: Task? = taskRepository.get(TaskDAOStub.FIRST_TASK_ID)

            assertTrue(task != null && task.id == TaskDAOStub.FIRST_TASK_ID)
        }

    @Test
    fun `when get non-existent task by id, must be return null`() =
        runBlocking {
            val task: Task? = taskRepository.get(TaskDAOStub.INITIAL_TASKS_COUNT + 1)

            assertTrue(task == null)
        }

    private suspend fun getLastTaskList(): List<Task> = taskRepository.getTasks().take(1).last()

    private suspend fun getLastTodayTaskList(): List<Task> = taskRepository.getTodayTasks().take(1).last()
}
