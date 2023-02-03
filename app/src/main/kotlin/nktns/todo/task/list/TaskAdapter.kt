package nktns.todo.task.list

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import nktns.todo.R
import nktns.todo.base.format
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
            outdateText.text = task.completionDate.format("d MMMM HH:mm")
            if (task.isOutdated) {
                outdateText.setTextColor(Color.RED)
            }
            taskName.setTextColor(
                ContextCompat.getColor( // TODO
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

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        val binding = TaskItemBinding.bind(view)
        val holder = TaskHolder(binding) { position -> itemClickListener.onItemClick(tasks[position]) }
        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            val task = tasks[holder.bindingAdapterPosition]
            if (task.isCompleted != isChecked) {
                actionHandler.onTaskCompleted(
                    TaskEntity(
                        task.id,
                        task.name,
                        task.description,
                        task.creationDate,
                        task.completionDate,
                        !task.isCompleted,
                        task.catalogId
                    )
                )
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
