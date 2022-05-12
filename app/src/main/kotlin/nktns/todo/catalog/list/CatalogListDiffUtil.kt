package nktns.todo.catalog.list

import androidx.recyclerview.widget.DiffUtil
import nktns.todo.data.database.entity.Catalog

class CatalogListDiffUtil(
    private val oldList: List<Catalog>,
    private val newList: List<Catalog>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask: Catalog = oldList[oldItemPosition]
        val newTask: Catalog = newList[newItemPosition]
        return oldTask.id == newTask.id
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask: Catalog = oldList[oldItemPosition]
        val newTask: Catalog = newList[newItemPosition]
        return oldTask == newTask
    }
}
