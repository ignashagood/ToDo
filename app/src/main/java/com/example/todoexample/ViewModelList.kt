package com.example.todoexample

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import com.example.todoexample.adapters.State
import com.example.todoexample.adapters.Success
import com.example.todoexample.fragments.TaskActionHandler
import java.util.*
import kotlin.random.Random

class ViewModelList(application: Application) : ViewModel(), TaskActionHandler {

    var state: MutableLiveData<State> = MutableLiveData()

    init {
        updateList()
    }

    private fun updateList() {
        val newTaskList = emptyList<Task>()
        updateList(newTaskList)
    }

    fun addNewTask(string: String) {
        state.value.let {
            if (it is Success) {
                val newTaskList = it.taskList + Task(string, Random.nextInt(), false)
                updateList(newTaskList)
            }
        }
    }

    private fun updateList(newTaskList: List<Task>) {
        val currentTaskList: List<Task> = (state.value as? Success)?.taskList ?: emptyList()
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun getOldListSize(): Int {
                return currentTaskList.size
            }

            override fun getNewListSize(): Int {
                return newTaskList.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldTask: Task = currentTaskList[oldItemPosition]
                val newTask: Task = newTaskList[newItemPosition]
                return oldTask.name == newTask.name
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldTask: Task = currentTaskList[oldItemPosition]
                val newTask: Task = newTaskList[newItemPosition]
                return oldTask == newTask
            }

        })
        state.value = Success(newTaskList, diffResult)
    }

    override fun onTaskCompleted(task: Task) {
        state.value.let { successState ->
            if (successState is Success) {
                val newTaskList = successState.taskList.toMutableList().apply {
                    this.remove(task)
                    add(Task(task.name, task.itemId, !task.isCompleted))
                    sortBy { it.isCompleted }
                }
                updateList(newTaskList)
            }
        }
    }

}