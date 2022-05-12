package nktns.todo.catalog.list

import androidx.recyclerview.widget.DiffUtil
import nktns.todo.data.database.entity.CatalogEntity

class CatalogListDiffUtil(
    private val oldList: List<CatalogEntity>,
    private val newList: List<CatalogEntity>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask: CatalogEntity = oldList[oldItemPosition]
        val newTask: CatalogEntity = newList[newItemPosition]
        return oldTask.id == newTask.id
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask: CatalogEntity = oldList[oldItemPosition]
        val newTask: CatalogEntity = newList[newItemPosition]
        return oldTask == newTask
    }
}
