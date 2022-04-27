package com.example.todoexample.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoexample.R
import com.example.todoexample.database.entity.TaskEntity
import com.example.todoexample.databinding.TaskItemBinding
import com.example.todoexample.fragments.ListFragment

class TaskAdapter(private val actionHandler: ListFragment.TaskActionHandler) :
    RecyclerView.Adapter<TaskAdapter.TaskHolder>() {

    class TaskHolder(private val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: TaskEntity) = with(binding) {
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

    private var tasks: List<TaskEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        val binding = TaskItemBinding.bind(view)
        val holder = TaskHolder(binding)
        binding.deleteBut.setOnClickListener {
            actionHandler.onTaskDeleteClick(tasks[holder.bindingAdapterPosition])
        }
        binding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            val task = tasks[holder.bindingAdapterPosition]
            if (task.isCompleted != isChecked) {
                actionHandler.onTaskCompleted(TaskEntity(task.name, task.itemId, !task.isCompleted))
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun updateList(newItems: Pair<List<TaskEntity>, DiffUtil.DiffResult>) {
        Log.d("Look", "TaskAdapter - updateList, ${Thread.currentThread().name}")
        tasks = newItems.first
        newItems.second.dispatchUpdatesTo(this)
    }
}

