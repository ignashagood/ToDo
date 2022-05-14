package nktns.todo.task.list

import androidx.recyclerview.widget.DiffUtil
import nktns.todo.data.database.entity.CatalogEntity
import nktns.todo.data.database.entity.TaskEntity

class MyDiffUtil<T>(
    private val oldList: List<T>,
    private val newList: List<T>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask: T = oldList[oldItemPosition]
        val newTask: T = newList[newItemPosition]
        var result = true
        if (oldTask is TaskEntity && newTask is TaskEntity) {
            result = oldTask.itemId == newTask.itemId
        }
        if (oldTask is CatalogEntity && newTask is CatalogEntity) {
            result = oldTask.id == newTask.id
        }
        return result
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask: T = oldList[oldItemPosition]
        val newTask: T = newList[newItemPosition]
        return oldTask == newTask
    }
}
