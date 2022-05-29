package nktns.todo.catalog.card.bottom

sealed class CatalogCardBottomState {
    object InitialLoading : CatalogCardBottomState()

    data class Content(
        val name: String,
        val hideCompletedTasks: Boolean,
        val actionName: String,
        val canDelete: Boolean
    ) : CatalogCardBottomState()
}
