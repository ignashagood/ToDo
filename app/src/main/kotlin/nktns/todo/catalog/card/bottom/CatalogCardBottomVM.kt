package nktns.todo.catalog.card.bottom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nktns.todo.R
import nktns.todo.base.ResourceProvider
import nktns.todo.data.CatalogRepository
import nktns.todo.data.database.entity.CatalogEntity
import java.util.Date

class CatalogCardBottomVM(
    private val resourceProvider: ResourceProvider,
    private val repository: CatalogRepository,
    private val mode: CatalogCardBottomMode,
) : ViewModel() {

    private var _action: MutableLiveData<CatalogCardBottomAction> = MutableLiveData()
    private var _state: MutableLiveData<CatalogCardBottomState> = MutableLiveData(CatalogCardBottomState.InitialLoading)

    val action: LiveData<CatalogCardBottomAction> by ::_action
    val state: LiveData<CatalogCardBottomState> by ::_state

    init {
        when (mode) {
            is CatalogCardBottomMode.Create -> onCreateMode()
            is CatalogCardBottomMode.View -> onViewMode(mode.catalogId)
        }
    }

    private fun onCreateMode() {
        _state.value = CatalogCardBottomState.Content(
            name = "",
            hideCompletedTasks = false,
            mode.actionName
        )
    }

    private fun onViewMode(catalogId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.get(catalogId) != null) {
                _state.postValue(repository.get(catalogId)!!.toContentState())
            } else TODO()
        }
    }

    private fun addCatalog(catalog: CatalogEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.add(catalog)
            _action.postValue(CatalogCardBottomAction.DISMISS)
        }
    }

    fun onSaveButtonClicked() {
        runOnContentState {
            when (mode) {
                is CatalogCardBottomMode.Create -> addCatalog(toEntity())
                is CatalogCardBottomMode.View -> TODO()
            }
        }
    }

    fun onCatalogNameChanged(newName: String) {
        runOnContentState { _state.value = copy(name = newName) }
    }

    private inline fun runOnContentState(block: CatalogCardBottomState.Content.() -> Unit) {
        val contentState: CatalogCardBottomState.Content? = _state.value as? CatalogCardBottomState.Content
        if (contentState != null) {
            block(contentState)
        }
    }

    private fun CatalogCardBottomState.Content.toEntity(id: Int = 0) = CatalogEntity(
        id,
        name,
        Date()
    )

    private val CatalogCardBottomMode.actionName: String
        get() =
            resourceProvider.getString(
                when (this) {
                    is CatalogCardBottomMode.Create -> R.string.catalog_card_bottom_title_add
                    is CatalogCardBottomMode.View -> R.string.catalog_card_bottom_title_change
                }
            )

    private fun CatalogEntity.toContentState() = CatalogCardBottomState.Content(
        name,
        hideCompletedTasks = true,
        actionName = mode.actionName,
    )
}