package com.example.todoexample

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import com.example.todoexample.database.MyDatabase
import com.example.todoexample.database.entity.TaskEntity
import com.example.todoexample.fragments.ListFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ViewModelList(application: Application) : AndroidViewModel(application),
    ListFragment.TaskActionHandler {

    private val repository: TaskRepository
    private var currentTasks: List<TaskEntity> = emptyList()
    val allTasks: LiveData<Pair<List<TaskEntity>, DiffUtil.DiffResult>>

    init {
        val taskDao = MyDatabase.get(application).taskDAO()
        repository = TaskRepository(taskDao)
        allTasks = Transformations.map(repository.sortedTasks) { newTasks ->
            val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int = currentTasks.size

                override fun getNewListSize(): Int = newTasks.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return currentTasks[oldItemPosition].itemId == newTasks[newItemPosition].itemId
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return currentTasks[oldItemPosition] == newTasks[newItemPosition]
                }
            })
            currentTasks = newTasks
            newTasks to diffResult
        }
    }

    fun addTask(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.add(task)
        }
    }

    override fun onTaskDeleteClick(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(task)
            Log.d("Look", "ViewModel - delete, ${Thread.currentThread().name}")
        }
    }

    override fun onTaskCompleted(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(task)
            Log.d("Look", "ViewModel - update, ${Thread.currentThread().name}")
        }

    }


}