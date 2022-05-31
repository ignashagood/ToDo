package nktns.todo.catalog.card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import nktns.todo.base.illegalState
import nktns.todo.data.CatalogRepository

class CatalogCardVM(
    private val repository: CatalogRepository,
    catalogId: Int
) : ViewModel() {
    private var _catalogName = MutableSharedFlow<String>()
    val catalogName: Flow<String> by ::_catalogName

    init {
        getCatalogName(catalogId)
    }

    private fun getCatalogName(catalogId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.get(catalogId).let {
                if (it != null) {
                    _catalogName.emit(it.name)
                } else {
                    illegalState("Unexpected catalog id")
                }
            }
        }
    }
}
