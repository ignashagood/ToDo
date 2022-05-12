package nktns.todo.catalog.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import nktns.todo.R
import nktns.todo.base.database.entity.Catalog
import nktns.todo.databinding.CatalogItemBinding

class CatalogListAdapter : RecyclerView.Adapter<CatalogListAdapter.CatalogHolder>() {

    class CatalogHolder(private val binding: CatalogItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(catalog: Catalog) = with(binding) {
            catalogName.text = catalog.name
        }
    }

    private var catalogs: List<Catalog> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.catalog_item, parent, false)
        val binding = CatalogItemBinding.bind(view)
        return CatalogHolder(binding)
    }

    override fun onBindViewHolder(holder: CatalogHolder, position: Int) {
        holder.bind(catalogs[position])
    }

    override fun getItemCount(): Int = catalogs.size

    @SuppressLint("NotifyDataSetChanged")
    fun initList(newCatalogList: List<Catalog>) {
        catalogs = newCatalogList
        notifyDataSetChanged()
    }

    fun updateList(newCatalogList: List<Catalog>, diffResult: DiffUtil.DiffResult) {
        catalogs = newCatalogList
        diffResult.dispatchUpdatesTo(this)
    }
}
