package nktns.todo.catalog.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            is CatalogEditorMode.View -> onViewMode(mode.catalog)
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

    private fun onViewMode(catalog: CatalogEntity) {
        viewModelScope.launch {
            _state.emit(catalog.toContentState())
        }
    }

    private fun addCatalog(catalog: CatalogEntity) {
        viewModelScope.launch {
            repository.add(catalog)
            _action.emit(CatalogEditorAction.DISMISS)
        }
    }

    private fun updateCatalog(catalog: CatalogEntity) {
        viewModelScope.launch {
            repository.update(catalog)
            _action.emit(CatalogEditorAction.DISMISS)
        }
    }

    private fun deleteCatalog(catalog: CatalogEntity) {
        viewModelScope.launch {
            repository.delete(catalog)
            _action.emit(CatalogEditorAction.DISMISS)
        }
    }

    fun onCompleteButtonClicked() {
        runOnContentState {
            when (mode) {
                is CatalogEditorMode.Create -> addCatalog(toEntity())
                is CatalogEditorMode.View -> updateCatalog(toEntity(mode.catalog.id))
            }
        }
    }

    fun onDeleteButtonClicked() {
        runOnContentState {
            when (mode) {
                is CatalogEditorMode.Create -> illegalState("Delete button cannot be visible")
                is CatalogEditorMode.View -> deleteCatalog(mode.catalog)
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
