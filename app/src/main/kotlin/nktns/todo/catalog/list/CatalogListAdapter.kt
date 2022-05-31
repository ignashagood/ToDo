package nktns.todo.catalog.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import nktns.todo.R
import nktns.todo.data.database.subset.CatalogWithCounts
import nktns.todo.databinding.CatalogItemBinding

class CatalogListAdapter(private val itemClickListener: CatalogListAdapter.OnItemClickListener) :
    RecyclerView.Adapter<CatalogListAdapter.CatalogHolder>() {
    interface OnItemClickListener {
        fun onItemClick(catalog: CatalogWithCounts)
    }

    class CatalogHolder(private val binding: CatalogItemBinding, clickHandler: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(catalog: CatalogWithCounts) = with(binding) {
            catalogName.text = catalog.catalog.name
            val outdatedTasks = catalog.outdatedTaskCount
            if (outdatedTasks == 0) {
                taskCount.text = catalog.taskCount.toString()
            } else {
                taskCount.text = "${catalog.taskCount} задач | ${catalog.outdatedTaskCount} задач"
            }
        }

        init {
            binding.root.setOnClickListener {
                clickHandler(bindingAdapterPosition)
            }
        }
    }

    private var catalogs: List<CatalogWithCounts> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.catalog_item, parent, false)
        val binding = CatalogItemBinding.bind(view)
        return CatalogHolder(binding) { position -> itemClickListener.onItemClick(catalogs[position]) }
    }

    override fun onBindViewHolder(holder: CatalogHolder, position: Int) {
        holder.bind(catalogs[position])
    }

    override fun getItemCount(): Int = catalogs.size

    @SuppressLint("NotifyDataSetChanged")
    fun initList(newCatalogList: List<CatalogWithCounts>) {
        catalogs = newCatalogList
        notifyDataSetChanged()
    }

    fun updateList(newCatalogList: List<CatalogWithCounts>, diffResult: DiffUtil.DiffResult) {
        catalogs = newCatalogList
        diffResult.dispatchUpdatesTo(this)
    }
}
