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
import nktns.todo.data.TaskRepository
import nktns.todo.data.database.entity.TaskEntity

class TaskListVM(application: Application, private val repository: TaskRepository) :
    AndroidViewModel(application),
    TaskListFragment.TaskActionHandler {
    private var _state: MutableLiveData<TaskListState> = MutableLiveData(TaskListState.InitialLoading)
    val state: LiveData<TaskListState> by ::_state

    init {
        viewModelScope.launch(Dispatchers.Main) {
            repository.sortedTasks.collect { newTasks ->
                val currentTaskList: List<TaskEntity> = (state.value as? TaskListState.Content)?.taskList ?: emptyList()
                val result: DiffUtil.DiffResult = DiffUtil.calculateDiff(TaskListDiffUtil(currentTaskList, newTasks))
                _state.value = TaskListState.Content(newTasks, result)
            }
        }
    }

    override fun onTaskDeleteClick(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(task)
        }
    }

    override fun onTaskCompleted(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(task)
        }
    }
}