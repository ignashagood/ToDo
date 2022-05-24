package nktns.todo.task.card

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nktns.todo.R
import nktns.todo.data.CatalogRepository
import nktns.todo.data.TaskRepository
import nktns.todo.data.database.entity.TaskEntity
import java.util.Calendar
import java.util.Date

class TaskCardVM(
    private val resourceProvider: nktns.todo.base.ResourceProvider,
    private val taskRepository: TaskRepository,
    private val catalogRepository: CatalogRepository,
    private val taskCardMode: TaskCardMode,
) : ViewModel() {

    private var _action: MutableLiveData<TaskCardAction> = MutableLiveData()
    private var _state: MutableLiveData<TaskCardState> = MutableLiveData(TaskCardState.InitialLoading)

    val action: LiveData<TaskCardAction> by ::_action
    val state: LiveData<TaskCardState> by ::_state

    init {
        when (taskCardMode) {
            is TaskCardMode.Create -> onCreateMode(taskCardMode.catalogId)
            is TaskCardMode.View -> onViewMode(taskCardMode.taskId)
        }
    }

    private fun onCreateMode(catalogId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val catalogName = catalogRepository.get(catalogId)?.name
            val cal = Calendar.getInstance()
            _state.postValue(
                TaskCardState.Content(
                    name = "",
                    isCompleted = false,
                    taskCardMode.actionName,
                    canDelete = false,
                    cal.time,
                    catalogName
                )
            )
        }
    }

    private fun onViewMode(taskId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.get(taskId)?.let {
                _state.postValue(it.toContentState())
            } // TODO()
        }
    }

    private fun addTask(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.add(task)
            _action.postValue(TaskCardAction.Dismiss)
        }
    }

    private fun updateTask(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.update(task)
            _action.postValue(TaskCardAction.Dismiss)
        }
    }

    private fun deleteTask(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.delete(task)
            _action.postValue(TaskCardAction.Dismiss)
        }
    }

    // Так правильней, чем прокидывать новое имя в onButtonClicked
    // Т.к. кнопку пользователь нажимает позже, а модель данных нужно зафиксировать сразу после изменения
    // Самая очевидная нужда в этом - при повороте экрана последнее
    // значение будет у нас на руках, а не только в EditText
    fun onTaskNameChanged(newName: String) {
        runOnContentState { _state.value = copy(name = newName) }
    }

    fun onSaveAddButtonClicked() {
        runOnContentState {
            when (taskCardMode) {
                is TaskCardMode.Create -> addTask(toEntity())
                is TaskCardMode.View -> updateTask(toEntity(taskCardMode.taskId))
            }
        }
    }

    fun onDeleteButtonClicked() {
        runOnContentState {
            when (taskCardMode) {
                is TaskCardMode.View -> deleteTask(toEntity(taskCardMode.taskId))
                is TaskCardMode.Create -> Unit // TODO
            }
        }
    }

    fun onDateButtonClicked() {
        runOnContentState {
            _action.value = TaskCardAction.ShowDatePicker(this.completionDate)
        }
    }

    fun onDatePicked(date: Calendar) {
        runOnContentState {
            val newCompletionDate = Calendar.getInstance().run {
                time = completionDate
                set(Calendar.YEAR, date.get(Calendar.YEAR))
                set(Calendar.MONTH, date.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH))
                time
            }
            _state.value = copy(completionDate = newCompletionDate)
        }
    }

    fun onTimeButtonClicked() {
        runOnContentState {
            _action.value = TaskCardAction.ShowTimePicker(this.completionDate)
        }
    }

    fun onTimePicked(time: Calendar) {
        runOnContentState {
            val newCompletionDate = Calendar.getInstance().run {
                this.time = completionDate
                set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY))
                set(Calendar.MINUTE, time.get(Calendar.MINUTE))
                this.time
            }
            _state.value = copy(completionDate = newCompletionDate)
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

    private fun TaskEntity.toContentState() = TaskCardState.Content(
        name,
        isCompleted,
        taskCardMode.actionName,
        true,
        completionDate,
        ""
    )

    private fun TaskCardState.Content.toEntity(id: Int = 0) = TaskEntity(
        id,
        name,
        "",
        Date(),
        completionDate,
        isCompleted,
        id
    )
}
