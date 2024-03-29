package nktns.todo.catalog.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import nktns.todo.base.diff.calculateDiff
import nktns.todo.data.CatalogRepository
import nktns.todo.data.database.subset.CatalogWithCounts

class CatalogListVM(application: Application, private val repository: CatalogRepository) :
    AndroidViewModel(application) {
    private val _state = MutableStateFlow<CatalogListState>(CatalogListState.InitialLoading)
    val state: StateFlow<CatalogListState> by ::_state

    init {
        viewModelScope.launch {
            repository.getAllWithCounts().collect { newCatalogList ->
                val currentCatalogList: List<CatalogWithCounts> =
                    (state.value as? CatalogListState.Content)?.catalogList ?: emptyList()
                val result: DiffUtil.DiffResult = calculateDiff(currentCatalogList, newCatalogList) { this.catalog.id }
                _state.value = CatalogListState.Content(newCatalogList, result)
            }
        }
    }
}
