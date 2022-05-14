package nktns.todo.catalog.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import nktns.todo.base.diff.calculateDiff
import nktns.todo.data.CatalogRepository
import nktns.todo.data.database.entity.CatalogEntity

class CatalogListVM(application: Application, private val repository: CatalogRepository) :
    AndroidViewModel(application) {
    private val _state: MutableLiveData<CatalogListState> = MutableLiveData(CatalogListState.InitialLoading)
    val state: LiveData<CatalogListState> by ::_state

    init {
        viewModelScope.launch(Dispatchers.Main) {
            repository.getAll().collect { newCatalogList ->
                val currentCatalogList: List<CatalogEntity> =
                    (state.value as? CatalogListState.Content)?.catalogList ?: emptyList()
                val result: DiffUtil.DiffResult = calculateDiff(currentCatalogList, newCatalogList, CatalogEntity::id)
                _state.value = CatalogListState.Content(newCatalogList, result)
            }
        }
    }
}
