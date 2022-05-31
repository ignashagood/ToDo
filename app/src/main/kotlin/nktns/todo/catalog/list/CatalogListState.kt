package nktns.todo.catalog.list

import androidx.recyclerview.widget.DiffUtil
import nktns.todo.data.database.subset.CatalogWithCounts

sealed class CatalogListState {
    object InitialLoading : CatalogListState()

    data class Content(
        val catalogList: List<CatalogWithCounts>,
        val diffResult: DiffUtil.DiffResult
    ) : CatalogListState()
}
