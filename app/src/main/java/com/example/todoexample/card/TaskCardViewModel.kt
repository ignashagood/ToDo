package com.example.todoexample.card

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoexample.base.database.TaskRepository
import com.example.todoexample.base.database.entity.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskCardViewModel(
    private val taskCardMode: TaskCardMode,
    private val repository: TaskRepository
) : ViewModel() {

    private val _taskCardAction: MutableLiveData<TaskCardAction> = MutableLiveData()
    val taskCardAction: LiveData<TaskCardAction> by ::_taskCardAction
    private var _task: MutableLiveData<TaskEntity> = MutableLiveData(TaskEntity("", 0, false))
    val task: MutableLiveData<TaskEntity> by ::_task

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (taskCardMode is TaskCardMode.View) {
                task.postValue(repository.getById(taskCardMode.taskId))
            }
        }
    }

    private fun addTask(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.add(task)
            _taskCardAction.postValue(TaskCardAction.DISMISS)
        }
    }

    private fun updateTask(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(task)
            _taskCardAction.postValue(TaskCardAction.DISMISS)
        }
    }

    fun onButtonClicked(taskName: String) {
        when (taskCardMode) {
            is TaskCardMode.Create -> {
                addTask(TaskEntity(taskName, 0, false))
            }
            is TaskCardMode.View -> {
                val updatedTask = TaskEntity(taskName, task.value!!.itemId, task.value!!.isCompleted)
                updateTask(updatedTask)
            }
        }
    }
}
