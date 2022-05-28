package nktns.todo.task.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import nktns.todo.base.diff.calculateDiff
import nktns.todo.data.CatalogRepository
import nktns.todo.data.TaskRepository
import nktns.todo.data.database.entity.TaskEntity

class TaskListVM(
    application: Application,
    private val taskRepository: TaskRepository,
    private val catalogRepository: CatalogRepository,
    private val taskListMode: TaskListMode,
) : AndroidViewModel(application), TaskListFragment.TaskActionHandler {
    private var _action = MutableSharedFlow<TaskListAction>(extraBufferCapacity = 1)
    private var _state = MutableStateFlow<TaskListState>(TaskListState.InitialLoading)
    val action: Flow<TaskListAction> by ::_action
    val state: StateFlow<TaskListState> by ::_state

    init {
        when (taskListMode) {
            is TaskListMode.All -> onAllMode()
            is TaskListMode.Catalog -> onCatalogMode(taskListMode.catalogId)
            is TaskListMode.Today -> onTodayMode()
        }
    }

    private fun onAllMode() {
        viewModelScope.launch(Dispatchers.Main) {
            taskRepository.getTasks().collect { newTaskList ->
                val currentTaskList: List<TaskEntity> = (state.value as? TaskListState.Content)?.taskList ?: emptyList()
                val result: DiffUtil.DiffResult = calculateDiff(currentTaskList, newTaskList, TaskEntity::id)
                _state.value = TaskListState.Content(newTaskList, result)
            }
        }
    }

    private fun onCatalogMode(catalogId: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            val newTaskList = catalogRepository.getWithTasks(catalogId).let {
                it?.tasks ?: emptyList()
            }
            val currentTaskList: List<TaskEntity> = (state.value as? TaskListState.Content)?.taskList ?: emptyList()
            val result: DiffUtil.DiffResult = calculateDiff(currentTaskList, newTaskList, TaskEntity::id)
            _state.value = TaskListState.Content(newTaskList, result)
        }
    }

    private fun onTodayMode() {
        viewModelScope.launch(Dispatchers.Main) {
            taskRepository.getTodayTasks().collect { newTaskList ->
                val currentTaskList: List<TaskEntity> = (state.value as? TaskListState.Content)?.taskList ?: emptyList()
                val result: DiffUtil.DiffResult = calculateDiff(currentTaskList, newTaskList, TaskEntity::id)
                _state.value = TaskListState.Content(newTaskList, result)
            }
        }
    }

    override fun onTaskCompleted(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.update(task)
        }
    }

    fun onAddButtonClicked() {
        when (taskListMode) {
            is TaskListMode.Catalog -> _action.tryEmit(TaskListAction.ShowCreateBottomSheet(taskListMode.catalogId))
            is TaskListMode.All -> _action.tryEmit(TaskListAction.ShowCreateBottomSheet(0))
            is TaskListMode.Today -> _action.tryEmit(TaskListAction.ShowCreateBottomSheet(0))
        }
    }

    fun onItemClicked(task: TaskEntity) {
        _action.tryEmit(TaskListAction.ShowViewBottomSheet(task.id))
    }
}
