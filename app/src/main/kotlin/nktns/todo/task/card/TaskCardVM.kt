package nktns.todo.task.card

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nktns.todo.R
import nktns.todo.data.TaskRepository
import nktns.todo.data.database.entity.TaskEntity
import java.util.Date

class TaskCardVM(
    // Хорошо соблюдать порядок свойств конструктора по их характеру
    // Сначала идут общие компоненты
    private val resourceProvider: nktns.todo.base.ResourceProvider,
    // Затем идут компоненты, решающие сценарии
    private val repository: TaskRepository,
    // Затем идут параметры сценариев
    private val taskCardMode: TaskCardMode,
) : ViewModel() {

    private val _action: MutableLiveData<TaskCardAction> = MutableLiveData()
    private var _state: MutableLiveData<TaskCardState> = MutableLiveData(TaskCardState.InitialLoading)

    val action: LiveData<TaskCardAction> by ::_action
    val state: LiveData<TaskCardState> by ::_state

    init {
        when (taskCardMode) {
            is TaskCardMode.Create -> onCreateMode()
            is TaskCardMode.View -> onViewMode(taskCardMode.taskId)
        }
    }

    private fun onCreateMode() {
        _state.value = TaskCardState.Content(
            name = "",
            isCompleted = false,
            taskCardMode.actionName,
            canDelete = false,
            // TODO - для получения текущей даты,
            Date()
        )
    }

    private fun onViewMode(taskId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.postValue(repository.get(taskId)!!.toContentState())
        }
    }

    private fun addTask(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.add(task)
            _action.postValue(TaskCardAction.Dismiss)
        }
    }

    private fun updateTask(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(task)
            _action.postValue(TaskCardAction.Dismiss)
        }
    }

    private fun deleteTask(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(task)
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
            }
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
            // Любые строки достаём из strings с помощью ResourceProvider
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
    )

    private fun TaskCardState.Content.toEntity(id: Int = 0) = TaskEntity(
        id,
        name,
        "",
        Date(),
        Date(),
        isCompleted,
        id
    )
}
