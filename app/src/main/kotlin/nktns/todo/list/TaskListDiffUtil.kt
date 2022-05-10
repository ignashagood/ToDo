package nktns.todo.list

import androidx.recyclerview.widget.DiffUtil
import nktns.todo.data.database.entity.TaskEntity

class TaskListDiffUtil(
    private val oldList: List<TaskEntity>,
    private val newList: List<TaskEntity>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask: TaskEntity = oldList[oldItemPosition]
        val newTask: TaskEntity = newList[newItemPosition]
        return oldTask.name == newTask.name
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask: TaskEntity = oldList[oldItemPosition]
        val newTask: TaskEntity = newList[newItemPosition]
        return oldTask == newTask
    }
}
