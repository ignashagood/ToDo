package nktns.todo.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import nktns.todo.R
import nktns.todo.data.database.entity.TaskEntity
import nktns.todo.databinding.TaskItemBinding

class TaskAdapter(
    private val actionHandler: TaskListFragment.TaskActionHandler,
    private val itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<TaskAdapter.TaskHolder>() {
    interface OnItemClickListener {
        fun onItemClick(task: TaskEntity)
    }

    class TaskHolder(private val binding: TaskItemBinding, clickHandler: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
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

        init {
            binding.root.setOnClickListener {
                clickHandler(bindingAdapterPosition)
            }
        }
    }

    private var tasks: List<TaskEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        val binding = TaskItemBinding.bind(view)
        val holder = TaskHolder(binding) { position -> itemClickListener.onItemClick(tasks[position]) }
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

    override fun getItemCount(): Int = tasks.size

    @SuppressLint("NotifyDataSetChanged")
    fun initList(newTaskList: List<TaskEntity>) {
        tasks = newTaskList
        notifyDataSetChanged()
    }

    fun updateList(newTaskList: List<TaskEntity>, diffResult: DiffUtil.DiffResult) {
        tasks = newTaskList
        diffResult.dispatchUpdatesTo(this)
    }
}
