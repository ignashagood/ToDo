package nktns.todo.catalog.card.content

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nktns.todo.data.CatalogRepository

class CatalogCardContentVM(
    private val repository: CatalogRepository,
    catalogId: Int
) : ViewModel() {
    private var _catalogName: MutableLiveData<String> = MutableLiveData()
    val catalogName: LiveData<String> by ::_catalogName

    init {
        getCatalogName(catalogId)
    }

    private fun getCatalogName(catalogId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.get(catalogId)?.let {
                _catalogName.postValue(it.name)
            } // TODO
        }
    }
}
