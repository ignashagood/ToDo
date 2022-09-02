package nktns.todo.task.list

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import nktns.todo.base.AppEvent
import nktns.todo.base.EventBus
import nktns.todo.base.diff.calculateDiff
import nktns.todo.data.TaskRepository
import nktns.todo.data.database.entity.CatalogEntity
import nktns.todo.data.database.entity.TaskEntity

class TaskListVM(
    application: Application,
    private val taskRepository: TaskRepository,
    private val taskListMode: TaskListMode,
    private val eventBus: EventBus
) : AndroidViewModel(application), TaskListFragment.TaskActionHandler {
    private var _action = MutableSharedFlow<TaskListAction>(extraBufferCapacity = 1)
    private var _state = MutableStateFlow<TaskListState>(TaskListState.InitialLoading)
    val action: Flow<TaskListAction> by ::_action
    val state: StateFlow<TaskListState> by ::_state

    init {
        updateList()
        viewModelScope.launch(Dispatchers.IO) {
            eventBus.events.collectLatest {
                when (it) {
                    is AppEvent.UpdateTaskList -> {
                        Log.d(TAG, "Collect event - ")
                        updateList()
                    }
                    is AppEvent.ClearArchive -> deleteArchivedTasks()
                }
            }
        }
    }

    private fun updateList() {
        when (taskListMode) {
            is TaskListMode.All -> onAllMode()
            is TaskListMode.Catalog -> onCatalogMode(taskListMode.catalog)
            is TaskListMode.Today -> onTodayMode()
            is TaskListMode.Archive -> onArchiveMode()
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

    private fun onCatalogMode(catalog: CatalogEntity) {
        viewModelScope.launch(Dispatchers.Main) {
            taskRepository.getCatalogTasks(catalog.id).collect { newTaskList ->
                val currentTaskList: List<TaskEntity> = (state.value as? TaskListState.Content)?.taskList ?: emptyList()
                val result: DiffUtil.DiffResult = calculateDiff(currentTaskList, newTaskList, TaskEntity::id)
                _state.value = TaskListState.Content(newTaskList, result)
            }
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

    private fun onArchiveMode() {
        viewModelScope.launch(Dispatchers.Main) {
            taskRepository.getArchivedTasks().collect { newTaskList ->
                val currentTaskList: List<TaskEntity> = (state.value as? TaskListState.Content)?.taskList ?: emptyList()
                val result: DiffUtil.DiffResult = calculateDiff(currentTaskList, newTaskList, TaskEntity::id)
                _state.value = TaskListState.Content(newTaskList, result)
            }
        }
    }

    private fun deleteArchivedTasks() {
        viewModelScope.launch(Dispatchers.Main) {
            taskRepository.deleteArchived()
        }
    }

    override fun onTaskCompleted(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.update(task)
        }
    }

    fun onAddButtonClicked() {
        when (taskListMode) {
            is TaskListMode.Catalog -> _action.tryEmit(TaskListAction.ShowCreateBottomSheet(taskListMode.catalog))
            is TaskListMode.All -> _action.tryEmit(TaskListAction.ShowCreateBottomSheet(null))
            is TaskListMode.Today -> _action.tryEmit(TaskListAction.ShowCreateBottomSheet(null))
            is TaskListMode.Archive -> Unit
        }
    }

    fun onItemClicked(task: TaskEntity) {
        _action.tryEmit(TaskListAction.ShowViewBottomSheet(task.id))
    }
}
