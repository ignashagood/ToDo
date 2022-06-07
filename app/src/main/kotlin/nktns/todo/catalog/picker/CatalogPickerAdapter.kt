package nktns.todo.catalog.picker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nktns.todo.R
import nktns.todo.data.database.entity.CatalogEntity
import nktns.todo.databinding.CatalogItemPickBinding

class CatalogPickerAdapter(
    private val catalogs: List<CatalogEntity>,
    private val itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<CatalogPickerAdapter.CatalogHolder>() {

    interface OnItemClickListener {
        fun onItemClick(catalog: CatalogEntity)
    }

    class CatalogHolder(
        private val binding: CatalogItemPickBinding,
        clickHandler: (position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(catalog: CatalogEntity) = with(binding) {
            catalogName.text = catalog.name
        }

        init {
            binding.root.setOnClickListener {
                clickHandler(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.catalog_item_pick, parent, false)
        val binding = CatalogItemPickBinding.bind(view)
        return CatalogHolder(binding) { position -> itemClickListener.onItemClick(catalogs[position]) }
    }

    override fun onBindViewHolder(holder: CatalogHolder, position: Int) {
        holder.bind(catalogs[position])
    }

    override fun getItemCount(): Int = catalogs.size
}
