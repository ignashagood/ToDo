package nktns.todo.catalog.list

import androidx.recyclerview.widget.DiffUtil
import nktns.todo.data.database.entity.CatalogEntity

sealed class CatalogListState {
    object InitialLoading : CatalogListState()

    data class Content(val catalogList: List<CatalogEntity>, val diffResult: DiffUtil.DiffResult) : CatalogListState()
}
