package nktns.todo.catalog.list

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import nktns.todo.R
import nktns.todo.base.ResourceProvider
import nktns.todo.data.database.subset.CatalogWithCounts
import nktns.todo.databinding.CatalogItemBinding

class CatalogListAdapter(
    private val resourceProvider: ResourceProvider,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<CatalogListAdapter.CatalogHolder>() {
    interface OnItemClickListener {
        fun onItemClick(catalog: CatalogWithCounts)
    }

    class CatalogHolder(
        private val resourceProvider: ResourceProvider,
        private val binding: CatalogItemBinding,
        clickHandler: (position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(catalog: CatalogWithCounts) = with(binding) {
            val tasksCountStringSize = resourceProvider.getQuantityString(
                R.plurals.catalog_tasks_count,
                catalog.taskCount,
                catalog.taskCount
            ).length + 2
            if (catalog.outdatedTaskCount != 0) {
                val tasks = SpannableString(
                    "${
                    resourceProvider
                        .getQuantityString(
                            R.plurals.catalog_tasks_count,
                            catalog.taskCount,
                            catalog.taskCount
                        )
                    } ${
                    resourceProvider
                        .getQuantityString(
                            R.plurals.catalog_outdated_tasks_count,
                            catalog.outdatedTaskCount,
                            catalog.outdatedTaskCount
                        )
                    }"
                )
                tasks.setSpan(
                    ForegroundColorSpan(Color.RED),
                    tasksCountStringSize,
                    tasks.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                taskCount.text = tasks
            } else {
                taskCount.text = SpannableString(
                    resourceProvider
                        .getQuantityString(
                            R.plurals.catalog_tasks_count,
                            catalog.taskCount,
                            catalog.taskCount
                        )
                )
            }
            catalogName.text = catalog.catalog.name
        }

        init {
            binding.root.setOnClickListener {
                clickHandler(bindingAdapterPosition)
            }
        }
    }

    private var catalogs: List<CatalogWithCounts> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogListAdapter.CatalogHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.catalog_item, parent, false)
        val binding = CatalogItemBinding.bind(view)
        return CatalogHolder(
            resourceProvider,
            binding
        ) { position -> itemClickListener.onItemClick(catalogs[position]) }
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
