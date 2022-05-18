package nktns.todo.catalog.card.bottom

sealed class CatalogCardBottomState {
    object InitialLoading : CatalogCardBottomState()

    data class Content(
        val name: String,
        val hideFunctionActive: Boolean,
        val highlightFunctionActive: Boolean,
        val actionName: String
    ) : CatalogCardBottomState()
}
