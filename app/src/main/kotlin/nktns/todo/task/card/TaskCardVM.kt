package nktns.todo.task.card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import nktns.todo.R
import nktns.todo.base.applyPickedDate
import nktns.todo.base.applyPickedTime
import nktns.todo.base.illegalState
import nktns.todo.base.pickers.PickedDate
import nktns.todo.base.pickers.PickedTime
import nktns.todo.base.toPickedDate
import nktns.todo.base.toPickedTime
import nktns.todo.data.CatalogRepository
import nktns.todo.data.TaskRepository
import nktns.todo.data.database.entity.CatalogEntity
import nktns.todo.data.database.entity.TaskEntity
import java.util.Date

class TaskCardVM(
    private val resourceProvider: nktns.todo.base.ResourceProvider,
    private val taskRepository: TaskRepository,
    private val catalogRepository: CatalogRepository,
    private val taskCardMode: TaskCardMode,
) : ViewModel() {

    private val _action = MutableSharedFlow<TaskCardAction>(extraBufferCapacity = 1)
    private var _state = MutableStateFlow<TaskCardState>(TaskCardState.InitialLoading)

    val action: Flow<TaskCardAction> by ::_action
    val state: StateFlow<TaskCardState> by ::_state

    init {
        when (taskCardMode) {
            is TaskCardMode.Create -> onCreateMode(taskCardMode.catalog)
            is TaskCardMode.View -> onViewMode(taskCardMode.taskId)
        }
    }

    private fun onCreateMode(catalog: CatalogEntity?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (catalog != null) {
                _state.emit(
                    TaskCardState.Content(
                        name = "",
                        isCompleted = false,
                        taskCardMode.actionName,
                        canDelete = false,
                        Date(),
                        catalog.name,
                        catalog.id
                    )
                )
            } else {
                _state.emit(
                    TaskCardState.Content(
                        name = "",
                        isCompleted = false,
                        taskCardMode.actionName,
                        canDelete = false,
                        Date(),
                        "????????????",
                        null
                    )
                )
            }
        }
    }

    private fun onViewMode(taskId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.get(taskId)?.let { task ->
                task.catalogId.let { catalogId ->
                    if (catalogId != null) {
                        _state.emit(task.toContentState(catalogRepository.get(catalogId)))
                    } else {
                        _state.emit(task.toContentState(null))
                    }
                }
            }
        }
    }

    private fun addTask(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.add(task)
            _action.emit(TaskCardAction.Dismiss)
        }
    }

    private fun updateTask(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.update(task)
            _action.emit(TaskCardAction.Dismiss)
        }
    }

    private fun deleteTask(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.delete(task)
            _action.emit(TaskCardAction.Dismiss)
        }
    }

    fun onTaskNameChanged(newName: String) {
        runOnContentState { _state.value = copy(name = newName) }
    }

    fun onSaveAddButtonClicked() {
        runOnContentState {
            when (taskCardMode) {
                is TaskCardMode.Create -> {
                    addTask(toEntity())
                    _action.tryEmit(TaskCardAction.ScheduleNotification(toEntity()))
                }
                is TaskCardMode.View -> updateTask(toEntity(taskCardMode.taskId))
            }
        }
    }

    fun onDeleteButtonClicked() {
        runOnContentState {
            when (taskCardMode) {
                is TaskCardMode.View -> deleteTask(toEntity(taskCardMode.taskId))
                is TaskCardMode.Create -> illegalState("Delete button cannot be visible")
            }
        }
    }

    fun onDateButtonClicked() {
        runOnContentState {
            _action.tryEmit(TaskCardAction.ShowDatePicker(completionDate.toPickedDate()))
        }
    }

    fun onDatePicked(date: PickedDate) {
        runOnContentState {
            val newCompletionDate = completionDate.applyPickedDate(date)
            _state.value = copy(completionDate = newCompletionDate)
        }
    }

    fun onTimeButtonClicked() {
        runOnContentState {
            _action.tryEmit(TaskCardAction.ShowTimePicker(completionDate.toPickedTime()))
        }
    }

    fun onTimePicked(time: PickedTime) {
        runOnContentState {
            val newCompletionDate = completionDate.applyPickedTime(time)
            _state.value = copy(completionDate = newCompletionDate)
        }
    }

    fun onCatalogButtonClicked() {
        runOnContentState {
            viewModelScope.launch(Dispatchers.Main) {
                catalogRepository.getAll().collect {
                    _action.tryEmit(TaskCardAction.ShowCatalogPicker(it))
                }
            }
        }
    }

    fun onCatalogPicked(catalog: CatalogEntity) {
        runOnContentState {
            _state.value = copy(catalogName = catalog.name, catalogId = catalog.id)
        }
    }

    private inline fun runOnContentState(block: TaskCardState.Content.() -> Unit) {
        val contentState: TaskCardState.Content? = _state.value as? TaskCardState.Content
        if (contentState != null) {
            block(contentState)
        }
    }

    private val TaskCardMode.actionName: String
        get() =
            resourceProvider.getString(
                when (this) {
                    is TaskCardMode.Create -> R.string.task_card_add_btn
                    is TaskCardMode.View -> R.string.task_card_save_btn
                }
            )

    private fun TaskEntity.toContentState(catalog: CatalogEntity?): TaskCardState {
        if (catalog != null) {
            return TaskCardState.Content(
                name,
                isCompleted,
                taskCardMode.actionName,
                true,
                completionDate,
                catalog.name,
                catalog.id
            )
        } else {
            return TaskCardState.Content(
                name,
                isCompleted,
                taskCardMode.actionName,
                true,
                completionDate,
                "????????????",
                null
            )
        }
    }

    private fun TaskCardState.Content.toEntity(id: Int = 0) = TaskEntity(
        id,
        name,
        "",
        Date(),
        completionDate,
        isCompleted,
        catalogId
    )
}
