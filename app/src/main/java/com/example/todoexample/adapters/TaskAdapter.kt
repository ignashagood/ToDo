package com.example.todoexample.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoexample.R
import com.example.todoexample.Task
import com.example.todoexample.databinding.TaskItemBinding
import com.example.todoexample.fragments.TaskActionHandler


class TaskAdapter(private val actionHandler: TaskActionHandler) :
    RecyclerView.Adapter<TaskAdapter.TaskHolder>() {

    private var taskList: List<Task> = emptyList()

    class TaskHolder(private val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) = with(binding) {
            taskName.text = task.name
            taskName.setTextColor(
                ContextCompat.getColor(
                    root.context,
                    if (task.isCompleted) R.color.grey else R.color.black
                )
            )
            checkbox.isChecked = task.isCompleted
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        val binding = TaskItemBinding.bind(view)
        val holder = TaskHolder(binding)
        binding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            actionHandler.onTaskCompleted(taskList[holder.bindingAdapterPosition])
        }
        return holder
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(taskList[position])
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun updateList(tasks: List<Task>, diffResult: DiffUtil.DiffResult) {
        taskList = tasks
        diffResult.dispatchUpdatesTo(this)
    }
}

sealed class State
class Success(val taskList: List<Task>, val diffResult: DiffUtil.DiffResult) : State()
class Loading : State()