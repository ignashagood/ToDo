package nktns.todo.base.diff

import androidx.recyclerview.widget.DiffUtil

class UniversalDiffCallback<T : Any>(
    private val oldList: List<T>,
    private val newList: List<T>,
    private val itemIdProvider: T.() -> Any
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask: T = oldList[oldItemPosition]
        val newTask: T = newList[newItemPosition]
        return itemIdProvider.invoke(oldTask) == itemIdProvider.invoke(newTask)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask: T = oldList[oldItemPosition]
        val newTask: T = newList[newItemPosition]
        return oldTask == newTask
    }
}
