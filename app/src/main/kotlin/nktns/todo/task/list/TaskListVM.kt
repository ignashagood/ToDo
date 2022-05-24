package nktns.todo.task.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.Dispatchers
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
    private var _action: MutableLiveData<TaskListAction> = MutableLiveData()
    private var _state: MutableLiveData<TaskListState> = MutableLiveData(TaskListState.InitialLoading)
    val action: LiveData<TaskListAction> by ::_action
    val state: LiveData<TaskListState> by ::_state

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
            val newTaskList = catalogRepository.getWithTasks(catalogId)!!.tasks
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
        if (taskListMode is TaskListMode.Catalog) {
            _action.postValue(TaskListAction.ShowCreateBottomSheet(taskListMode.catalogId))
        } else {
            _action.value = TaskListAction.ShowCreateBottomSheet(0)
        }
    }

    fun onItemClicked(task: TaskEntity) {
        _action.value = TaskListAction.ShowViewBottomSheet(task.id)
    }
}
