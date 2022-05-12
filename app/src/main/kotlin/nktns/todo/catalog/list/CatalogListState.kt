package nktns.todo.catalog.list

import androidx.recyclerview.widget.DiffUtil
import nktns.todo.data.database.entity.Catalog

sealed class CatalogListState {
    object InitialLoading : CatalogListState()

    data class Content(val catalogList: List<Catalog>, val diffResult: DiffUtil.DiffResult) : CatalogListState()
}
