package nktns.todo.catalog.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import nktns.todo.R
import nktns.todo.base.ResourceProvider
import nktns.todo.base.illegalState
import nktns.todo.data.CatalogRepository
import nktns.todo.data.database.entity.CatalogEntity
import java.util.Date

class CatalogEditorVM(
    private val resourceProvider: ResourceProvider,
    private val repository: CatalogRepository,
    private val mode: CatalogEditorMode,
) : ViewModel() {

    private var _action = MutableSharedFlow<CatalogEditorAction>()
    private var _state = MutableStateFlow<CatalogEditorState>(CatalogEditorState.InitialLoading)

    val action: Flow<CatalogEditorAction> by ::_action
    val state: Flow<CatalogEditorState> by ::_state

    init {
        when (mode) {
            is CatalogEditorMode.Create -> onCreateMode()
            is CatalogEditorMode.View -> onViewMode(mode.catalogId)
        }
    }

    private fun onCreateMode() {
        _state.value = CatalogEditorState.Content(
            catalogName = "",
            hideCompletedTasks = false,
            mode.completedActionName,
            false
        )
    }

    private fun onViewMode(catalogId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val catalog: CatalogEntity? = repository.get(catalogId)
            if (catalog != null) {
                _state.emit(catalog.toContentState())
            } else {
                illegalState("Unexpected catalog id")
                _action.emit(CatalogEditorAction.DISMISS)
            }
        }
    }

    private fun addCatalog(catalog: CatalogEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.add(catalog)
            _action.emit(CatalogEditorAction.DISMISS)
        }
    }

    private fun updateCatalog(catalog: CatalogEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(catalog)
            _action.emit(CatalogEditorAction.DISMISS)
        }
    }

    private fun deleteTask(catalog: CatalogEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(catalog)
            _action.emit(CatalogEditorAction.DISMISS)
        }
    }

    fun onCompleteButtonClicked() {
        runOnContentState {
            when (mode) {
                is CatalogEditorMode.Create -> addCatalog(toEntity())
                is CatalogEditorMode.View -> updateCatalog(toEntity(mode.catalogId))
            }
        }
    }

    fun onDeleteButtonClicked() {
        runOnContentState {
            when (mode) {
                is CatalogEditorMode.Create -> illegalState("Delete button cannot be visible")
                is CatalogEditorMode.View -> deleteTask(toEntity(mode.catalogId))
            }
        }
    }

    fun onCatalogNameChanged(newName: String) {
        runOnContentState { _state.value = copy(catalogName = newName) }
    }

    private inline fun runOnContentState(block: CatalogEditorState.Content.() -> Unit) {
        val contentState: CatalogEditorState.Content? = _state.value as? CatalogEditorState.Content
        if (contentState != null) {
            block(contentState)
        }
    }

    private fun CatalogEditorState.Content.toEntity(id: Int = 0) = CatalogEntity(
        id,
        catalogName,
        Date()
    )

    private val CatalogEditorMode.completedActionName: String
        get() =
            resourceProvider.getString(
                when (this) {
                    is CatalogEditorMode.Create -> R.string.catalog_editor_title_add
                    is CatalogEditorMode.View -> R.string.catalog_editor_title_change
                }
            )

    private fun CatalogEntity.toContentState() = CatalogEditorState.Content(
        catalogName = name,
        hideCompletedTasks = true,
        completedActionName = mode.completedActionName,
        canDelete = true
    )
}
